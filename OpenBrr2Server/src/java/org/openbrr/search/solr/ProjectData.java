package org.openbrr.search.solr;

public class ProjectData {
	private int id;
	private String name;
	private String desc;
	private String url;
	
	public ProjectData(int _id, String _name, String _desc, String _url) {
		id = _id;
		name = _name;
		desc = _desc;
		url = _url;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return super.toString()
			+" - id: "+id
			+", name: "+name
			+", desc: "+desc
			+", url: "+url;
	}
	
	
}
