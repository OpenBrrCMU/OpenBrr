package org.openbrr.collector.flossmole.data;

import java.util.List;

/**
 * ProjectDetail POJO is used to capture all the data related to a 
 * specific project to be used by the object indexer.
 * 
 * The All the project attributes like (license, os, code, etc.)
 * are indexed by their IDs and parent IDs
 * 
 * @author ashutosh
 *
 */
public interface ProjectDetail {
	public Integer getProjectId();
	public String getName();
	public String getDescription();
	public String getUrl();
	public List<Integer> getLicenseIds();
	public List<Integer> getTopicIds();
	public List<Integer> getProgLangIds();
	public List<Integer> getLangIds();
	public List<Integer> getDataStoreIds();
	public List<Integer> getAudienceIds();
	public List<Integer> getOsIds();
	public List<Integer> getIntfIds();
}
