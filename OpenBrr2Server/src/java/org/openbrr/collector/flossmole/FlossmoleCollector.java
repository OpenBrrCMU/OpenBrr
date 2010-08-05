package org.openbrr.collector.flossmole;

import java.util.Collection;
import java.util.List;

import org.openbrr.collector.CollectionInfo;
import org.openbrr.collector.flossmole.crawler.Analyzer;
import org.openbrr.collector.flossmole.crawler.Crawler;
import org.openbrr.collector.flossmole.crawler.FileInfo;
import org.openbrr.core.util.IOUtil;
import org.openbrr.core.util.XhtmlUtil;

/**
 * @author Admin
 * 
 * This class contains the main method and hence we need to run this class in order to launch the OpenBrr2Server.  
 * On running, this class first calls the crawler that crawls the FlossMole DB in order to identify the files that 
 * match the search input. Crawler then downloads all the identified files to the local file system. 
 * Then the FlossmoleDataProcessor is called. This class unzips the files and then parses them. When parsing, 
 * it reads the files into memory, parses files to fetch the required fields into memory and then stores them 
 * in the local DB.  
 * 
 */

public class FlossmoleCollector {

	
	/**
	 * This method crawls the Flossmole DB in order to identify the files that need to be downloaded. 
	 * @return
	 */
	public CollectionInfo gatherData() {
		CollectionInfo info = new CollectionInfo();
		
		List<String> fileUrlList = new Crawler().getFileList();
		System.out.println("Files Identified by Crawler: "+fileUrlList);
		
		Collection<String> fileListToProcess = new Analyzer().analyzeFiles(fileUrlList);
		System.out.println("Files Identified to Process: "+ fileListToProcess.size());
		
		downloadFiles(fileListToProcess);
		
		FlossmoleDataProcessor.processData();
		
		return info;
	}
	
	public static void downloadFiles(Collection<String> fileListToProcess) {
		for(String fileUrl : fileListToProcess){
			FileInfo info = new FileInfo(fileUrl);
			if(!info.hasParseError()) {
				System.out.println("downloading file: "+info.getFileUrl());
				XhtmlUtil.downloadFile(fileUrl, FlossmoleConstants.UNPROCESSED_FOLDER+"/"+info.getFileName());
			} else {
				System.out.println("error downloading file: "+info.getFileUrl());
			}
		}
		IOUtil.uncompressFilesInFolder(FlossmoleConstants.UNPROCESSED_FOLDER);
	}

	public static void main(String[] args) {
		FlossmoleCollector fc = new FlossmoleCollector();
		fc.gatherData();
	}
}
