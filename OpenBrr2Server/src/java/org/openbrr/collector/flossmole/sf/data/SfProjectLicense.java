package org.openbrr.collector.flossmole.sf.data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sf_project_licenses")
public class SfProjectLicense {

	@Id 
	@Column(name="project_id")
	Integer projectId;

	@Column(name="lic_id")
	Integer licId;
	
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updatedAt;
}
