package org.openbrr.core.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openbrr.collector.flossmole.data.ProjectAttribute;

@Entity
@Table(name="operating_systems")
public class OperatingSystem implements ProjectAttribute {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="os_id")
	Integer osId;
	
	@Column(name="name")
	String name;

	@Column(name="family_id")
	Integer familyId;
	
	@Column(name="code")
	String code;

	@SuppressWarnings("unused")
	private OperatingSystem() {
		//for hibernate
	}
	
	public OperatingSystem(String _name) {
		name = _name;
	}

	public Integer getId() {
		return osId;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
}
