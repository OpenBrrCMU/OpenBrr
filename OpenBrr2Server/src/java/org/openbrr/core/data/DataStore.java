package org.openbrr.core.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openbrr.collector.flossmole.data.ProjectAttribute;

@Entity
@Table(name="data_stores")
public class DataStore implements ProjectAttribute {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ds_id")
	Integer dsId;
	
	@Column(name="name")
	String name;

	@Column(name="code")
	String code;

	@SuppressWarnings("unused")
	private DataStore() {
		//for hibernate
	}
	
	public DataStore(String _name) {
		name = _name;
	}

	public Integer getId() {
		return dsId;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
}
