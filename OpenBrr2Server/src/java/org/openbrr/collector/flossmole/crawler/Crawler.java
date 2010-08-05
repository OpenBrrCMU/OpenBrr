package org.openbrr.collector.flossmole.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openbrr.collector.flossmole.FlossmoleConstants;
import org.openbrr.core.util.HtmlParser;
import org.openbrr.core.util.XhtmlUtil;
import org.w3c.dom.Node;

/**
 * 
 * @author Admin
 * This class crawls the FlossMole DB (BASE_URL specified in the FlossmoleConstants)in order to identify the files 
 * that match the search input. It then downloads all the identified files to the local file system. 
 */


public class Crawler {
	
	private boolean hasMore;
	private List<String> fileList;
	
	public Crawler() {
		fileList = new ArrayList<String>();
	}
	
	public synchronized List<String> getFileListInPage(int _pageNum) {
		List<String> fileList = new ArrayList<String>();
		
		String url = FlossmoleConstants.BASE_URL + "?start=" + (_pageNum - 1)*100;
		System.out.println("Fetching Data from URL : "+url);
		HtmlParser parser = new HtmlParser(url);
		
		/*
		 * Determine if there are more pages. Page information is present in the following format;
		 * 
		 * <div class="pagination">1 - 100 of 397 <a href="list?start=100">Next <b>&rsaquo;</b></a></div>
		 * 
		 * and there are two of these; one on top and another at the bottom of the table.
		 */
		List<Node> pageNodes = parser.getNodes("//div[@class=\"pagination\"]");
		Node pageNode = (pageNodes != null && pageNodes.size() > 0)? pageNodes.get(0) : null;
		String nodeValue = (pageNode != null)? XhtmlUtil.getNodeData(pageNode.getLastChild()) : null;
		hasMore = (nodeValue != null) && ("Next".equals(nodeValue.trim()));
		
		/*
		 * Read the files
		 */
		List<Node> rowNodes = parser.getNodes("//table/tbody/tr");
		for(Node row : rowNodes) {
			List<Node> linkNodes = parser.getNodes(row, "td/a");
			for(Node linkNode : linkNodes) {
				String linkValue = XhtmlUtil.getNodeData(linkNode);
				if(linkValue.endsWith(".bz2")) {
					String listedUrl = linkNode.getAttributes().getNamedItem("href").getNodeValue();
					String downloadableUrl = fetchDownloadableURL(listedUrl);
					fileList.add(downloadableUrl);
					System.out.println("link: " + downloadableUrl);
				}
				
			}
		}
		System.out.println("has more Pages: "+hasMore);
		System.out.println("Number of files: "+fileList.size());
		//Node pageLink = parser.getNode(pa, _path)
		return fileList;
	}
	
	
	private synchronized String fetchDownloadableURL(String url){
		Pattern urlPattern = Pattern.compile(".*?name=(.*?)&.*?");
		Matcher urlMatcher = urlPattern.matcher(url);
		String fileName = "";
		if (urlMatcher.find()) {
			fileName = urlMatcher.group(1);
		}
		return FlossmoleConstants.DOWNLOAD_BASE_URL + fileName;
	}
	
	public List<String> getFileList() {
		int page = 0;
		do {
			page++;
			List<String> newList = getFileListInPage(page);
			if(newList.size() == 0) {
				break;
			} else {
				fileList.addAll(newList);
			}
		} while(hasMore);
		System.out.println("Total number of files: "+fileList.size());
		return fileList;
	}

	//for testing
	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.getFileList();
		//System.out.println(crawler.fetchDownloadableURL("detail?name=rfProjectList2010-May.txt.bz2&can=2&q="));
	}
	
}
