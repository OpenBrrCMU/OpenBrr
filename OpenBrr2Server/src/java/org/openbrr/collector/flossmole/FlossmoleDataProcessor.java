package org.openbrr.collector.flossmole;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openbrr.collector.flossmole.sf.SfDataProcessor;
import org.openbrr.core.db.PersistenceUtil;
import org.openbrr.search.solr.ObrrSolrServer;

/**
 * 
 * @author Admin
 * This class drops the old data before trying to initiate the data load process
 *
 */

public class FlossmoleDataProcessor {

	public static void processData() {
		new SfDataProcessor().processData();
	}

	//for testing
	public static void resetData() {
		Session session = null;
		Transaction tx = null;
		
		String[] cleanupSqls = {
				"truncate projects",
				"truncate topics",
				"truncate licenses",
				"truncate operating_systems",
				"truncate audiences",
				"truncate data_stores",
				"truncate program_languages",
				"truncate interfaces",
				
				"truncate sf_project_details",
				"truncate sf_project_topics",
				"truncate sf_project_data_stores",
				"truncate sf_project_licenses",
				"truncate sf_project_oses",
				"truncate sf_project_prog_langs",
				
				"truncate sf_project_licenses",
				"truncate sf_project_oses"
		};
		
		try {
			session = PersistenceUtil.getSession();
			tx = session.beginTransaction();
			
			for(String sql : cleanupSqls) {
				session.createSQLQuery(sql).executeUpdate();
			}
			tx.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	public static void indexData() {
		SfDataProcessor.indexAllProjects();
		ObrrSolrServer.commit();
	}

	public static void resetIndex() {
		ObrrSolrServer.reset();
		ObrrSolrServer.commit();
	}

}
