package org.openbrr.collector.flossmole.sf.data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sf_project_stats")
public class SfProjectStat {

	@Id 
	@Column(name="project_id")
	Integer projectId;

	@Column(name="data_dt")
	Timestamp dataDt;
	
	@Column(name="data_period")
	String dataPeriod;
	
	@Column(name="downloads")
	Integer downloads;
	
	@Column(name="open_bugs")
	Integer openBugs;
	
	@Column(name="total_bugs")
	Integer totalBugs;
	
	@Column(name="open_sr")
	Integer openSr;
	
	@Column(name="total_sr")
	Integer totalSr;
	
	@Column(name="open_patches")
	Integer openPatches;
	
	@Column(name="total_patches")
	Integer totalPatches;
	
	@Column(name="open_features")
	Integer openFeatures;
	
	@Column(name="total_features")
	Integer totalFeatures;
	
	@Column(name="forums")
	Integer forums;
	
	@Column(name="forum_messages")
	Integer forumMessages;
	
	@Column(name="mailing_lists")
	Integer mailingLists;
	
	@Column(name="cvs_commits")
	Integer cvs_commits;
	
	@Column(name="cvs_reads")
	Integer cvs_reads;
	
	@Column(name="svn_commits")
	Integer svn_commits;
	
	@Column(name="svn_reads")
	Integer svn_reads;
	
	private SfProjectStat() {
		//for hibernate
	}
	
}
