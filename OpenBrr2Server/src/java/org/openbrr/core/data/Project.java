package org.openbrr.core.data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="projects")
public class Project {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="project_id")
	Integer projectId;
	
	@Column(name="host")
	String host;
	
	@Column(name="code")
	String code;
	
	@Column(name="primary_project_id")
	Integer primaryProjectId;
	
	@Column(name="last_update")
	Timestamp lastUpdate;
	
	public Project() {
		//for hibernate
	}
	
	public Project(String _host, String _code) {
		host = _host;
		if(_code.length() > 48) {
			System.out.println("Project Code > 48: "+_code);
			code = _code.substring(0, 48);
		} else {
			code = _code;
		}
		
		lastUpdate = new Timestamp(System.currentTimeMillis());
	}
	
	public Integer getProjectId() {
		return projectId;
	}

	public String getHost() {
		return host;
	}

	public String getCode() {
		return code;
	}

	public Integer getPrimaryProjectId() {
		return primaryProjectId;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
	
	
}
