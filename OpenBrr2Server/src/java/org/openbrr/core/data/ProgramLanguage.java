package org.openbrr.core.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openbrr.collector.flossmole.data.ProjectAttribute;

@Entity
@Table(name="program_languages")
public class ProgramLanguage implements ProjectAttribute {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pl_id")
	Integer plId;
	
	@Column(name="name")
	String name;

	@Column(name="code")
	String code;

	@SuppressWarnings("unused")
	private ProgramLanguage() {
		//for hibernate
	}
	
	public ProgramLanguage(String _name) {
		name = _name;
	}

	public Integer getId() {
		return plId;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
}
