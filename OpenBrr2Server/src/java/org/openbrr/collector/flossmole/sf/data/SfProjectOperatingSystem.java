package org.openbrr.collector.flossmole.sf.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sf_project_oses")
public class SfProjectOperatingSystem {

	@Id 
	@Column(name="project_id")
	Integer projectId;
	
	@Column(name="os_id")
	Integer osId;
	
	@Column(name="name")
	String name;

	@Column(name="family_id")
	Integer familyId;
	
	@Column(name="code")
	String code;

	private SfProjectOperatingSystem() {
		//for hibernate
	}
	
}
