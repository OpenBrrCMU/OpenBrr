package org.openbrr.collector.flossmole.sf.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sf_project_audiences")
public class SfProjectAudience {

	@Id 
	@Column(name="project_id")
	Integer projectId;
	
	@Column(name="audience_id")
	Integer audienceId;
	
	@Column(name="name")
	String name;

	@Column(name="code")
	String code;

	private SfProjectAudience() {
		//for hibernate
	}

}
