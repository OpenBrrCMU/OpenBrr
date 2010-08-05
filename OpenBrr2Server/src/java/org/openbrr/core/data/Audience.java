package org.openbrr.core.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openbrr.collector.flossmole.data.ProjectAttribute;

@Entity
@Table(name="audiences")
public class Audience implements ProjectAttribute {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="audience_id")
	Integer audienceId;
	
	@Column(name="name")
	String name;

	@Column(name="code")
	String code;

	@SuppressWarnings("unused")
	private Audience() {
		//for hibernate
	}
	
	public Audience(String _name) {
		name = _name;
	}

	public Integer getId() {
		return audienceId;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
}
