package org.openbrr.collector.flossmole.sf.data;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openbrr.collector.flossmole.data.ProjectDetail;

@Entity
@Table(name="sf_project_details")
public class SfProjectDetail implements ProjectDetail {

	@Id 
	@Column(name="project_id")
	Integer projectId;

	@Column(name="name")
	String name;
	
	@Column(name="description")
	String description;
	
	@Column(name="registered_dt")
	Timestamp registeredDt;
	
	@Column(name="curr_version")
	String currentVersion;
	
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updatedAt;
	
	public SfProjectDetail() {
		//for hibernate
	}

	public SfProjectDetail(int _id, String _name, String _desc) {
		projectId = _id;
		
		setName(_name);
		
		if(_desc.length() > 250) {
			description = _desc.substring(0, 250);
		} else {
			description = _desc;
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		createdAt = now;
		updatedAt = now;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Timestamp getRegisteredDt() {
		return registeredDt;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setName(String _name) {
		if(_name != null && _name.length() > 71) {
			name = _name.substring(0, 71);
		} else {
			name = _name;
		}
	}

	public void setRegisteredDt(Timestamp registeredDt) {
		this.registeredDt = registeredDt;
	}

	public List<Integer> getAudienceIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> getDataStoreIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> getIntfIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> getLangIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> getLicenseIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> getOsIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> getProgLangIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> getTopicIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
