package org.openbrr.search;

import java.util.List;

import org.openbrr.collector.flossmole.data.AttrDataCache;
import org.openbrr.collector.flossmole.data.ProjectAttribute;
import org.openbrr.core.data.DataStore;
import org.openbrr.core.data.License;
import org.openbrr.core.data.OperatingSystem;
import org.openbrr.core.data.ProgramLanguage;
import org.openbrr.core.data.Topic;

/**
 * ProjectIndexer interface is used to capture all the data related to a 
 * specific project to be used by the object indexer.
 * 
 * The All the project attributes like (license, os, code, etc.)
 * are indexed by their IDs and parent IDs
 * 
 * @author ashutosh
 *
 */
public class ProjectIndexerData {
	private int projectId;
	private String name;
	private String desc;
	private String url;
	private List<Integer> topicIds;
	private List<Integer> audienceIds;
	private List<Integer> dataStoreIds;
	private List<Integer> osIds;
	private List<Integer> intfIds;
	private List<Integer> langIds;
	private List<Integer> licenseIds;
	private List<Integer> progLangIds;
	
	public ProjectIndexerData(int _id, String _name, String _desc) {
		projectId = _id;
		name = _name;
		desc = _desc;
	}
	
	public List<Integer> getAudienceIds() {
		return audienceIds;
	}

	public List<Integer> getDataStoreIds() {
		return dataStoreIds;
	}

	public String getDescription() {
		return desc;
	}

	public List<Integer> getIntfIds() {
		return intfIds;
	}

	public List<Integer> getLangIds() {
		return langIds;
	}

	public List<Integer> getLicenseIds() {
		return licenseIds;
	}

	public String getName() {
		return name;
	}

	public List<Integer> getOsIds() {
		return osIds;
	}

	public List<Integer> getProgLangIds() {
		return progLangIds;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public List<Integer> getTopicIds() {
		return topicIds;
	}

	public String getUrl() {
		return url;
	}

	public void setAudienceIds(List<Integer> _ids) {
		audienceIds = _ids;
	}

	public void setDataStoreIds(List<Integer> _ids) {
		dataStoreIds = _ids;
	}

	public void setIntfIds(List<Integer> _ids) {
		intfIds = _ids;
	}

	public void setLangIds(List<Integer> _ids) {
		langIds = _ids;
	}

	public void setLicenseIds(List<Integer> _ids) {
		licenseIds = _ids;
	}

	public void setOsIds(List<Integer> _ids) {
		osIds = _ids;
	}

	public void setProgLangIds(List<Integer> _ids) {
		progLangIds = _ids;
	}

	public void setTopicIds(List<Integer> _ids) {
		topicIds = _ids;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String _desc) {
		desc = _desc;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@SuppressWarnings("unchecked")
	public String toXml() {
		Object[][] meta = new Object[][] {
				{License.class, "<lic>", "</lic>", licenseIds},
				{OperatingSystem.class, "<os>", "</os>", osIds},
				{ProgramLanguage.class, "<code>", "</code>", progLangIds},
				{DataStore.class, "<ds>", "</ds>", dataStoreIds},
				{Topic.class, "<topic>", "</topic>", topicIds}
		};
		
		StringBuilder str = new StringBuilder("<project ");
		str.append("id=\"").append(projectId).append("\">");
		
		str.append("<name>").append(name).append("</name>");
		str.append("<desc>").append(desc).append("</desc>");
		str.append("<url>").append((url==null)? "" : url).append("</url>");
		
		for(Object[] attrMeta : meta) {
			Class<? extends ProjectAttribute> attrClass = (Class<? extends ProjectAttribute>)attrMeta[0];
			List<Integer> attrIds = (List<Integer>) attrMeta[3];
			
			str.append(attrMeta[1]);
			if(attrIds != null && attrIds.size()>0) {
				for(Integer attrId : attrIds) {
					ProjectAttribute attr = AttrDataCache.find(attrClass, attrId);
					if(attr != null && attr.getCode() != null) {
						str.append(' ').append(attr.getCode());
					}
					
				}
			}
			str.append(attrMeta[2]);
		}
		str.append("</project>");
		return str.toString();
	}
}
