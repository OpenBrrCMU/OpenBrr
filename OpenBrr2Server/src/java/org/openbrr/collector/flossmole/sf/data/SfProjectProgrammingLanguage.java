package org.openbrr.collector.flossmole.sf.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sf_project_prog_langs")
public class SfProjectProgrammingLanguage {

	@Id
	@Column(name = "project_id")
	Integer projectId;

	@Column(name = "pl_id")
	Integer plId;

	@Column(name = "name")
	String name;

	@Column(name = "code")
	String code;

	private SfProjectProgrammingLanguage() {
		// for hibernate
	}
}
