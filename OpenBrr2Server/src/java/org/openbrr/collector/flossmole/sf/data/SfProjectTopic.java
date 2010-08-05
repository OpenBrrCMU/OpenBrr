package org.openbrr.collector.flossmole.sf.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sf_project_topics")
public class SfProjectTopic{
	
	@Id 
	@Column(name="project_id")
	Integer projectId;
	
	@Column(name="topic_id")
	Integer topicId;
	
	@Column(name="name")
	String name;

	@Column(name="parent_id")
	Integer parentId;
	
	@Column(name="category_id")
	Integer categoryId;
	
	@Column(name="code")
	String code;

	private SfProjectTopic() {
		//for hibernate
	}
	
}
