package org.openbrr.collector.flossmole.sf.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sf_project_urls")
public class SfProjectUrl {

	@Id 
	@Column(name="project_id")
	Integer projectId;

	@Column(name="url_type")
	String url_type;
	
	@Column(name="url_status")
	String url_status;
	
	@Column(name="url")
	String url;
	
	private SfProjectUrl() {
	}
	
}
