package org.openbrr.core.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="interfaces")
public class Interface {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="intf_id")
	Integer intfId;
	
	@Column(name="name")
	String name;

	@SuppressWarnings("unused")
	private Interface() {
		//for hibernate
	}
	
	public Interface(String _name) {
		name = _name;
	}
}
