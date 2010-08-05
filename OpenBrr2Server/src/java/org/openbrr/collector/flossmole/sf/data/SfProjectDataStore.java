package org.openbrr.collector.flossmole.sf.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sf_project_data_stores")
public class SfProjectDataStore {

	@Id 
	@Column(name="project_id")
	Integer projectId;
	
	@Column(name = "ds_id")
	Integer dsId;

	@Column(name = "name")
	String name;

	@Column(name = "code")
	String code;

	private SfProjectDataStore() {
		// for hibernate
	}

}
