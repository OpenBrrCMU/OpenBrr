package org.openbrr.search.solr;

import java.util.ArrayList;
import java.util.List;

import org.openbrr.search.ProjectIndexerData;

public class SearchResult {

	private List<ProjectIndexerData> projectList;
	
	public SearchResult() {
		projectList = new ArrayList<ProjectIndexerData>();
	}
	
	public void add(ProjectIndexerData _data) {
		projectList.add(_data);
	}
	
	public String toXml() {
		StringBuilder str = new StringBuilder("<projects>");
		for(ProjectIndexerData proj : projectList) {
			str.append(proj.toXml());
		}
		str.append("</projects>");
		return str.toString();
	}
}
