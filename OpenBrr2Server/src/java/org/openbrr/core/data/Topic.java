package org.openbrr.core.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openbrr.collector.flossmole.data.ProjectAttribute;

@Entity
@Table(name="topics")
public class Topic implements ProjectAttribute {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
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

	@SuppressWarnings("unused")
	private Topic() {
		//for hibernate
	}
	
	public Topic(String _name) {
		name = _name;
	}

	public Integer getId() {
		return topicId;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
}
