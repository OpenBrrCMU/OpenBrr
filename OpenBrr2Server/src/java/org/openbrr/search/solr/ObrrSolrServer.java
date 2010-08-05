package org.openbrr.search.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.openbrr.search.ProjectIndexerData;

public class ObrrSolrServer {

	private static SolrServer server;
	
	static {
		try {
			//File configFile = new File("/home/ashutosh/projects/obrr_server/ext/solr/conf/solrconfig.xml");
			//CoreContainer core = new CoreContainer("/home/ashutosh/projects/obrr_server/data/solr/", "");
			//server = new EmbeddedSolrServer(core, "");
			
			server = new CommonsHttpSolrServer("http://localhost:8080/solr");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SearchResult queryResult(String _query) {
		SearchResult result = new SearchResult();
		List<ProjectIndexerData> projects = query(_query);
		for(ProjectIndexerData data: projects) {
			result.add(data);
		}
		
		return result;
	}
	
	public static List<ProjectIndexerData> query(String _query) {
		
		List<ProjectIndexerData> results = new ArrayList<ProjectIndexerData>();
		
		SolrQuery sq = new SolrQuery();
		sq.setQuery(_query);
		sq.setFacet(true);
		sq.addFacetField(IndexFields.ID);
		sq.addFacetField(IndexFields.NAME);
		sq.addFacetField(IndexFields.DESC);
		sq.addFacetField(IndexFields.URL);
		sq.addFacetField(IndexFields.LIC_IDS);
		sq.addFacetField(IndexFields.TOPIC_IDS);
		sq.addFacetField(IndexFields.OS_IDS);
		sq.addFacetField(IndexFields.AUD_IDS);
		sq.addFacetField(IndexFields.PL_IDS);
		sq.addFacetField(IndexFields.DS_IDS);
		
		//sq.setIncludeScore(true);
		
		try {
			QueryResponse qr = server.query(sq);
			
			SolrDocumentList sdl = qr.getResults();
			
			for(SolrDocument d : sdl) {
				ProjectIndexerData data = new ProjectIndexerData(
						(Integer) d.getFieldValue(IndexFields.ID),
						(String) d.getFieldValue(IndexFields.NAME),
						(String) d.getFieldValue(IndexFields.DESC)
				);
				
				data.setLicenseIds(stringToList((String)(d.getFieldValue(IndexFields.LIC_IDS))));
				data.setOsIds(stringToList((String)(d.getFieldValue(IndexFields.OS_IDS))));
				data.setTopicIds(stringToList((String)(d.getFieldValue(IndexFields.TOPIC_IDS))));
				data.setAudienceIds(stringToList((String)(d.getFieldValue(IndexFields.AUD_IDS))));
				data.setProgLangIds(stringToList((String)(d.getFieldValue(IndexFields.PL_IDS))));
				data.setDataStoreIds(stringToList((String)(d.getFieldValue(IndexFields.DS_IDS))));

				results.add(data);
			}
		} catch(SolrServerException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	
	public static void post(ProjectIndexerData _project) {
		Collection<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
		docList.add(convertToSolrInputDoc(_project));
		try {
			server.add(docList);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		commit();
	}
	
	public static void reset() {
		try {
			server.deleteByQuery("*:*");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void commit() {
		try {
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static SolrInputDocument convertToSolrInputDoc(ProjectIndexerData _proj) {
		SolrInputDocument solrDoc = new SolrInputDocument();
		solrDoc.addField(IndexFields.ID, _proj.getProjectId());
		solrDoc.addField(IndexFields.NAME, _proj.getName(), 5);
		solrDoc.addField(IndexFields.DESC, _proj.getDescription(), 1);
		solrDoc.addField(IndexFields.LIC_IDS, listToString(_proj.getLicenseIds()), 1);
		solrDoc.addField(IndexFields.TOPIC_IDS, listToString(_proj.getTopicIds()), 1);
		solrDoc.addField(IndexFields.OS_IDS, listToString(_proj.getOsIds()), 1);
		solrDoc.addField(IndexFields.PL_IDS, listToString(_proj.getProgLangIds()), 1);
		solrDoc.addField(IndexFields.DS_IDS, listToString(_proj.getDataStoreIds()), 1);
		solrDoc.addField(IndexFields.AUD_IDS, listToString(_proj.getAudienceIds()), 1);
		
		return solrDoc;
	}
	
	private static String listToString(List<Integer> _ids) {
		StringBuilder str = new StringBuilder();
		
		if(_ids != null && _ids.size()>0) {
			for(Integer id : _ids) {
				str.append(" ").append(id);
			}
		}
		return  str.toString();
	}
	
	private static List<Integer> stringToList(String _value) {
		List<Integer> ids = new ArrayList<Integer>();
		if(_value == null || _value.trim().length() == 0) {
			return ids;
		}
		
		if(_value.trim().indexOf(' ') == -1) {
			//no space present
			ids.add(new Integer(_value.trim()));
		} else {
			StringTokenizer st = new StringTokenizer(_value, " ");
			while(st.hasMoreTokens()) {
				ids.add(new Integer(st.nextToken()));
			}
		}
		return ids;
	}
	
	public static void main(String[] args) {
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		ids.add(2);
		ids.add(3);
		
		System.out.println("ids:"+listToString(ids));
		
		System.out.println("length: "+stringToList(" 1 12 24 34").size());
	}
}
