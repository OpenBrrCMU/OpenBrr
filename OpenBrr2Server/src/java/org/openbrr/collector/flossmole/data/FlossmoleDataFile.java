package org.openbrr.collector.flossmole.data;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openbrr.collector.flossmole.crawler.FileInfo;
import org.openbrr.core.db.PersistenceUtil;

/**
 * 
 * @author Admin
 * Thie class maps with the 'flossmole_data_files' table in the database. 
 */


@Entity
@Table(name="flossmole_data_files")
public class FlossmoleDataFile {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	Integer id;
	
	@Column(name="url")
	String url;
	
	@Column(name="name")
	String name;

	@Column(name="collected_on")
	Timestamp collectedOn;

	@Column(name="processed_on")
	Timestamp processedOn;

	@Column(name="status")
	String status;
	
	private FlossmoleDataFile() {
		//for hibernate
	}

	public FlossmoleDataFile(String url, String name, Timestamp collectedOn,
			Timestamp processedOn, String status) {
		super();
		this.url = url;
		this.name = name;
		this.collectedOn = collectedOn;
		this.processedOn = processedOn;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public Timestamp getCollectedOn() {
		return collectedOn;
	}

	public Timestamp getProcessedOn() {
		return processedOn;
	}

	public String getStatus() {
		return status;
	}
	
	@SuppressWarnings("unchecked")
	public static List<FlossmoleDataFile> getAll() {
		Session session = PersistenceUtil.getSession();
		List<FlossmoleDataFile> dataFiles = null;
		
		try {
			dataFiles = (List<FlossmoleDataFile>) session.createQuery(
						"from FlossmoleDataFile data")
						.list(); 
		} finally {
			if(session != null)
				session.close();
		}
		return dataFiles;
	}
	
	public static FlossmoleDataFile getDataFileByUrl(String _url) {
		Session session = PersistenceUtil.getSession();
		
		FlossmoleDataFile dataFile = null;
		
		try {
			dataFile = (FlossmoleDataFile) session.createQuery(
						"from FlossmoleDataFile data where data.url = ?")
						.setString(0, _url)
						.uniqueResult(); 
		} finally {
			if(session != null)
				session.close();
		}
		return dataFile;
	}
	
	public static void updateFileStatus(List<FileInfo> _fileInfoList) {
		
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceUtil.getSession();
			tx = session.beginTransaction();
			
			for(FileInfo fileInfo : _fileInfoList) {
				FlossmoleDataFile file = getDataFileByUrl(fileInfo.getFileUrl());
				
				if(file == null) {
					file = new FlossmoleDataFile();
					file.url = fileInfo.getFileUrl();
				}
				
				if(fileInfo.hasParseError()) {
					file.status = fileInfo.getParseError();
				}
				
				session.saveOrUpdate(file);
			}
			tx.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}
}
