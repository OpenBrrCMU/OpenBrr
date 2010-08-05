package org.openbrr.collector.flossmole.sf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openbrr.collector.flossmole.FlossmoleConstants;
import org.openbrr.collector.flossmole.data.ProjectAttribute;
import org.openbrr.collector.flossmole.sf.data.SfProjectDetail;
import org.openbrr.core.data.Audience;
import org.openbrr.core.data.DataStore;
import org.openbrr.core.data.License;
import org.openbrr.core.data.OperatingSystem;
import org.openbrr.core.data.ProgramLanguage;
import org.openbrr.core.data.Project;
import org.openbrr.core.data.Topic;
import org.openbrr.core.db.PersistenceUtil;
import org.openbrr.search.ProjectIndexerData;
import org.openbrr.search.solr.ObrrSolrServer;

/**
 * 
 * @author Admin
 * 
 *
 *
 */

public class SfDataProcessor {

	private Map<String, Integer> codeIdMap;
	private Map<Integer, SfProjectDetail> idDetailMap;
	
	private static boolean testRun = true;
	private static int testRunCount = 100;
	private static int batchMarker = 500;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //2009-01-31 21:36:07
	
	private Logger logger = Logger.getLogger(getClass());
	
	public SfDataProcessor() {
		codeIdMap = new HashMap<String, Integer>();
		idDetailMap = new HashMap<Integer, SfProjectDetail>();
	}
	
	@SuppressWarnings("unchecked")
	public void processData() {
		
		Session session = null;
		try {
			session = PersistenceUtil.getSession();
			List<Project> sfProjects = (List<Project>) session.createQuery(
				"from Project proj where proj.host = ?")
				.setString(0, "sf").list();
			
			for(Project proj : sfProjects) {
				codeIdMap.put(proj.getCode(), proj.getProjectId());
			}
			
			List<SfProjectDetail> sfProjectDetails = (List<SfProjectDetail>) session.createQuery(
				"from SfProjectDetail detail")
				.list();
			
			for(SfProjectDetail detail : sfProjectDetails) {
				idDetailMap.put(detail.getProjectId(), detail);
			}
			
			BufferedReader fr = null;
			try {
				//time to update; start transaction
				//tx = session.getTransaction();
				//tx.begin();
				
				processSfProjectDesc(session);
				
				/* Process License Info
				 * sfRawLicenseData has the following fields;
				 * 		proj_unixname, code, description, date_collected, datasource_id
				 */
				processSfProjectAttributeData(session, "sfRawLicenseData", "sf_project_licenses", License.class, 0, 2);
				
				/* Process OperatingSystem Data
				 * sfRawOpSysData has the following fields;
				 * 		proj_unixname, code, description, date_collected, datasource_id
				 */
				processSfProjectAttributeData(session, "sfRawOpSysData", "sf_project_oses", OperatingSystem.class, 0, 2);
				
				/* Process DbEnv Data
				 * sfRawDbEnvData has the following fields;
				 * 		proj_unixname, code, description, date_collected, datasource_id
				 */
				processSfProjectAttributeData(session, "sfRawDbEnvData", "sf_project_data_stores", DataStore.class, 0, 2);
				
				/* Process Topic Data
				 * sfRawTopicData has the following fields;
				 * 		proj_unixname, code, description, date_collected, datasource_id
				 */
				processSfProjectAttributeData(session, "sfRawTopicData", "sf_project_topics", Topic.class, 0, 2);
				
				/* Process IntAud Data
				 * sfRawIntAudData has the following fields;
				 * 		proj_unixname, code, description, date_collected, datasource_id
				 */
				processSfProjectAttributeData(session, "sfRawIntAudData", "sf_project_audiences", Audience.class, 0, 2);
				
				/* Process ProgLang Data
				 * sfRawProgLangData has the following fields;
				 * 		proj_unixname, code, description, date_collected, datasource_id
				 */
				processSfProjectAttributeData(session, "sfRawProgLangData", "sf_project_prog_langs", ProgramLanguage.class, 0, 2);
				
				processSfProjectInfo(session);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(fr != null)
						fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} finally {
			if (session != null)
				session.close();
		}
		
		
	}
	
	private String getDataFile(String _fileType) {
		String fileName = null;
		
		File unprocessedFolder = new File(FlossmoleConstants.UNPROCESSED_FOLDER);
		for(File file : unprocessedFolder.listFiles()) {
			if(file.getName().startsWith(_fileType)) {
				fileName = file.getName();
				break;
			}
		}
		
		return fileName;
	}
	
	private boolean isValid(String _line) {
		if(_line == null || _line.trim().length() == 0) {
			return false;
		}
		
		String[] invalidWords = {"#", "proj_unixname"};
		for(String invalidWord : invalidWords) {
			if(_line.startsWith(invalidWord)) {
				return false;
			}
		}
		
		return true;
	}
	
	private List<String> tokenize(String _line) {
		List<String> tokens = new ArrayList<String>();
		
		StringTokenizer st = new StringTokenizer(_line, "\t");
		while(st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}
		return tokens;
	}
	
	private void processSfProjectDesc(Session _session) throws HibernateException, IOException {
		//track time
		Timestamp start = new Timestamp(System.currentTimeMillis());
		System.out.println("\nProcessing sfProjectDesc data");
		
		Transaction tx = _session.beginTransaction();

		/*
		 * sfProjectDesc has the following fields;
		 * 		proj_unixname, description, datasource_id, date_collected
		 */
		BufferedReader fr = null;
		try {
			String proj_desc = getDataFile("sfProjectDesc");
			fr = new BufferedReader(
						new FileReader(
									new File(FlossmoleConstants.UNPROCESSED_FOLDER, proj_desc)));
			
			int recCount = 0;
			String lastLine = null;
			while(fr.ready()) {
				String line = fr.readLine();
				
				if(!isValid(line)) {
					continue;
				}
				
				if(lastLine != null) {
					line = lastLine + " " + line;
				}
				
				List<String> tokens = tokenize(line);
				if(tokens.size() < 4) {
					//incorrect data
					//logger.debug("Cannot process data : "+line);
					lastLine = line;
					continue;
				} else {
					lastLine = null;
				}
				
				if(tokens.get(1) == null)
					continue;
				
				Integer projectId = codeIdMap.get(tokens.get(0));
				if(projectId == null) {
					Project p = new Project("sf", tokens.get(0));
					_session.save(p);
					
					projectId = p.getProjectId();
					codeIdMap.put(tokens.get(0), p.getProjectId());
				}
				
				SfProjectDetail projDetail = idDetailMap.get(projectId);
				if(projDetail == null) {
					projDetail = new SfProjectDetail(projectId, null, tokens.get(1));
					_session.save(projDetail);
					
					idDetailMap.put(projectId, projDetail);
				}
				
				//for testing
				recCount++;
				if(testRun && recCount > testRunCount) {
					break;
				}
				
				if(recCount % batchMarker == 0) {
					System.out.print(".");
				}
			}
		} finally {
			if(fr != null)
				fr.close();
		}
		Timestamp end = new Timestamp(System.currentTimeMillis());
		System.out.println("Time taken : "+(end.getTime() - start.getTime())/1000+" sec");
		
		tx.commit();
	}
	
	private void processSfProjectInfo(Session _session) throws IOException {
		//track time
		Timestamp start = new Timestamp(System.currentTimeMillis());
		System.out.println("\nProcessing sfProjectInfo data");
		
		Transaction tx = _session.beginTransaction();
		/*
		 * sfProjectInfo has the following fields;
		 * 		proj_unixname, proj_long_name, date_registered, dev_count, date_collected
		 */
		String proj_info = getDataFile("sfProjectInfo");
		BufferedReader fr = null;
		try {
			fr = new BufferedReader(
						new FileReader(
									new File(FlossmoleConstants.UNPROCESSED_FOLDER, proj_info)));
			int recCount = 0;
			while(fr.ready()) {
				String line = fr.readLine();
				
				if(!isValid(line)) {
					continue;
				}
				
				List<String> tokens = tokenize(line);
				
				//check for the right number of tokens
				if(tokens.size() < 3) {
					System.out.println("Number of Tokens less than 3 in file 'sfProjectInfo'; line: "+line);
					continue;
				}
				
				Integer projectId = codeIdMap.get(tokens.get(0));
				if(projectId == null) {
					logger.debug("No Project found for code ["+tokens.get(0)+"] in sfProjectDesc. skipping sfProjectInfo data..");
					continue;
				}
				
				SfProjectDetail projDetail = idDetailMap.get(projectId);
				if(projDetail == null) {
					logger.debug("No Project found for code ["+tokens.get(0)+"] in sfProjectDesc. skipping sfProjectInfo data..");
					continue;
				} else {
					projDetail.setName(tokens.get(1));
					try {
						projDetail.setRegisteredDt(new Timestamp(sdf.parse(tokens.get(2)).getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					_session.update(projDetail);
					
					//ProjectIndexer.indexProject(projDetail);
				}
				
				//for testing
				recCount++;
				if(testRun && recCount > testRunCount) {
					break;
				}
				
				if(recCount % batchMarker == 0) {
					System.out.print(".");
				}
			}
		} finally {
			fr.close();
		}
		
		tx.commit();
		
		Timestamp end = new Timestamp(System.currentTimeMillis());
		System.out.println("Time taken : "+(end.getTime() - start.getTime())/1000+" sec");
	}
	
	@SuppressWarnings("unchecked")
	private <T extends ProjectAttribute> void processSfProjectAttributeData(Session _session, String _fileType, String _tableName, 
			Class<T> _class, int _projectCodePos, int _attrNamePos) throws IOException {
		/*
		 * sfProjectInfo has the following fields;
		 * 		proj_unixname, code, description, date_collected, datasource_id
		 */
		
		Map<String, Integer> attrNameIdMap = new HashMap<String, Integer>();
		Map<Integer, List<Integer>> projectAttrIdsMap = new HashMap<Integer, List<Integer>>();
		
		//track time
		Timestamp start = new Timestamp(System.currentTimeMillis());
		System.out.println("\nProcessing "+_fileType+" data");
		
		Transaction tx = _session.beginTransaction();
		
		String projAttrData = getDataFile(_fileType);
		BufferedReader fr = null;
		String line = null;
		
		try {
			System.out.println("Querying: "+_class.getSimpleName());
			List<ProjectAttribute> projAttrs = (List<ProjectAttribute>) _session.createQuery("from "+_class.getSimpleName()).list();
			for(ProjectAttribute attr : projAttrs) {
				attrNameIdMap.put(attr.getName(), attr.getId());
			}
			
			//read all the current data
			List<Object[]> data = _session.createSQLQuery("select * from " + _tableName).list();
			for(Object[] rec : data) {
				Integer projId = (Integer) rec[0];
				Integer attrId = (Integer) rec[1];
				
				List<Integer> attrIdList = projectAttrIdsMap.get(projId);
				if(attrIdList == null) {
					attrIdList = new ArrayList<Integer>();
					projectAttrIdsMap.put(projId, attrIdList);
				}
				attrIdList.add(attrId);
			}
			
			SQLQuery insertQuery = _session.createSQLQuery("insert into "+_tableName+" values(?, ?)");
			//Timestamp now = new Timestamp(System.currentTimeMillis());
			
			fr = new BufferedReader(
						new FileReader(
									new File(FlossmoleConstants.UNPROCESSED_FOLDER, projAttrData)));
			int recCount = 0;
			Constructor<T> attrConst = _class.getConstructor(String.class);
			
			while(fr.ready()) {
				try {
					line = fr.readLine();
					
					if(!isValid(line)) {
						continue;
					}
					
					List<String> tokens = tokenize(line);
					
					//check for the right number of tokens
					if(tokens.size() < 5) {
						System.out.println("Number of Tokens less than 3 in file: '"+_fileType+"'; line: "+line);
						continue;
					}

					Integer projectId = codeIdMap.get(tokens.get(_projectCodePos));
					String attrName = tokens.get(_attrNamePos);
					if(projectId == null) {
						logger.debug("No Project found for code ["+tokens.get(_projectCodePos)+"] in sfRawLicenseData. skipping data..");
						continue;
					}
					
					Integer attrId = attrNameIdMap.get(attrName);
					if(attrId == null) {
						ProjectAttribute attr = attrConst.newInstance(attrName);
						_session.save(attr);
						
						attrId = attr.getId();
						attrNameIdMap.put(attrName, attrId);
					}
					
					List<Integer> attrIds = projectAttrIdsMap.get(projectId);
					if(attrIds == null) {
						attrIds = new ArrayList<Integer>();
						projectAttrIdsMap.put(projectId, attrIds);
					}
					if(!attrIds.contains(attrId)) {
						insertQuery.setInteger(0, projectId)
								.setInteger(1, attrId)
								.executeUpdate();
						
						attrIds.add(attrId);
						
					}
					
					//for testing
					recCount++;
					if(testRun && recCount > testRunCount) {
						break;
					}
					
					if(recCount % batchMarker == 0) {
						System.out.print(".");
					}
				} catch (Exception e) {
					System.out.println("Error Processing line: "+line);
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			if(fr != null)
				fr.close();
		}
		
		tx.commit();
		
		Timestamp end = new Timestamp(System.currentTimeMillis());
		System.out.println("Time taken : "+(end.getTime() - start.getTime())/1000+" sec");
	}
	
	@SuppressWarnings("unchecked")
	public static void indexAllProjects() {
		
		Session session = null;
		String query_sf_topics = "select project_id, topic_id from sf_project_topics";
		String query_sf_licenses = "select project_id, lic_id from sf_project_licenses";
		String query_sf_oses = "select project_id, os_id from sf_project_oses";
		String query_sf_prog_langs = "select project_id, pl_id from sf_project_prog_langs";
		String query_sf_datastores = "select project_id, ds_id from sf_project_data_stores";
		String query_sf_audiences = "select project_id, aud_id from sf_project_audiences";
		
		String query_sf_details = "select project_id, name, description from sf_project_details";
		
		try {
			session = PersistenceUtil.getSession();
			
			HashMap<Integer, List<Integer>> sfTopicsIds = new HashMap<Integer, List<Integer>>();
			arrangeProjectData(session, query_sf_topics, sfTopicsIds);
			
			HashMap<Integer, List<Integer>> sfLicenseIds = new HashMap<Integer, List<Integer>>();
			arrangeProjectData(session, query_sf_licenses, sfLicenseIds);
			
			HashMap<Integer, List<Integer>> sfOsIds = new HashMap<Integer, List<Integer>>();
			arrangeProjectData(session, query_sf_oses, sfOsIds);
			
			HashMap<Integer, List<Integer>> sfProgLangIds = new HashMap<Integer, List<Integer>>();
			arrangeProjectData(session, query_sf_prog_langs, sfProgLangIds);
			
			HashMap<Integer, List<Integer>> sfDataStoreIds = new HashMap<Integer, List<Integer>>();
			arrangeProjectData(session, query_sf_datastores, sfDataStoreIds);
			
			HashMap<Integer, List<Integer>> sfAudienceIds = new HashMap<Integer, List<Integer>>();
			arrangeProjectData(session, query_sf_audiences, sfAudienceIds);
			
			//ScrollableResults sr = session.createSQLQuery(query_sf_details).scroll();
			//while(sr.next()) {
			//	Integer projId = sr.getInteger(0);
			//	String projName = sr.getString(1);
			//	String projDesc = sr.getString(2);
			
			List<Object[]> dataSet = session.createSQLQuery(query_sf_details).list();
			for(Object[] data: dataSet) {
				Integer projId = (Integer) data[0];
				ProjectIndexerData proj = new ProjectIndexerData(projId, (String)data[1], (String)data[2]);
				proj.setLicenseIds(sfLicenseIds.get(projId));
				proj.setTopicIds(sfTopicsIds.get(projId));
				proj.setOsIds(sfOsIds.get(projId));
				proj.setProgLangIds(sfProgLangIds.get(projId));
				proj.setDataStoreIds(sfDataStoreIds.get(projId));
				proj.setAudienceIds(sfAudienceIds.get(projId));
				
				ObrrSolrServer.post(proj);
			}
			//sr.close();
		//} catch (SQLException e) {
		//	e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void arrangeProjectData(Session _session, String _query, HashMap<Integer, List<Integer>> _projectAttrList) {
		System.out.println("Processing Query: "+_query);

		List<Object[]> dataSet = _session.createSQLQuery(_query).list();
		for(Object[] data : dataSet) {
				Integer projId = (Integer) data[0];
				Integer attrId = (Integer) data[1];
			
			List<Integer> attrIdList = _projectAttrList.get(projId);
			if(attrIdList == null) {
				attrIdList = new ArrayList<Integer>();
				_projectAttrList.put(projId, attrIdList);
			}
			attrIdList.add(attrId);
		}
		//sr.close();
	}

}
