package org.openbrr.collector.flossmole.crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openbrr.collector.flossmole.FlossmoleConstants;
import org.openbrr.collector.flossmole.data.FlossmoleDataFile;
import org.openbrr.core.util.IOUtil;

/**
 * 
 * @author Admin
 * Analyzer initiates the parsing activity
 */

public class Analyzer {
	
	private Map<String, Date> fileSignDate;
	private Map<String, String> fileSignUrl;
	
	public Analyzer() {
		fileSignDate = new HashMap<String, Date>();
		fileSignUrl = new HashMap<String, String>();
		
	}
	
	public Collection<String> analyzeFiles(List<String> _fileUrlList) {
		List<String> newFiles = new ArrayList<String>();
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		
		List<FlossmoleDataFile> processedFiles = FlossmoleDataFile.getAll();
		for(FlossmoleDataFile file : processedFiles) {
			updateCurrData(file);
		}
		
		//log all file analysis for debug/tracking
		FileWriter fw = null;
		try {
			File outFile = new File(FlossmoleConstants.DATA_FOLDER+"/file_analysis-"
					+FlossmoleConstants.sdf.format(Calendar.getInstance().getTime()));
			
//			if(outFile.exists()) {
//				outFile.delete();
//			}
//			if(outFile.createNewFile()) {
//				fw = new FileWriter(outFile);
//			}

			fw = new FileWriter(outFile);
			
			//check for new files
			for(String fileUrl : _fileUrlList) {
				
				//initial filter
				if(processedFiles.contains(fileUrl)) {
					continue;
				}
				
				FileInfo fileInfo = new FileInfo(fileUrl);
				if(fileInfo.hasParseError()) {
					//Processor cannot handle this type of file
					if(fw != null)
						fw.write(fileUrl+": error=>"+fileInfo.getParseError()+"\n");
				} else if(isMoreRecentFile(fileInfo)) {
					//Processor can handle this file
					newFiles.add(fileInfo.getFileUrl());
					if(fw != null)
						fw.write(fileUrl+": status=>to be processed.\n");
				} else {
					fileInfo.setStatus("Skipped: Older file");
					//old file
					if(fw != null)
						fw.write(fileUrl+": error=>skipped (old file)\n");
				}
				
				//FlossmoleDataFile.updateFileStatus(fileInfo);
				fileInfoList.add(fileInfo);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtil.closeWriter(fw);
		}
		
		FlossmoleDataFile.updateFileStatus(fileInfoList);
		return newFiles;
	}
	
	/*
	 * Checks if this is a more recent file in the same category.
	 */
	private boolean isMoreRecentFile(FileInfo _info) {
		
		String fileSign = _info.getProjectCode()+_info.getFileType();
		Date lastDate = fileSignDate.get(fileSign);
		boolean statusChange = false;
		
		if(lastDate == null || _info.getFileDate().after(lastDate)) {
			fileSignDate.put(fileSign, _info.getFileDate());
			fileSignUrl.put(fileSign, _info.getFileUrl());
			statusChange = true;
		}
		
		return statusChange;
	}
	
	private void updateCurrData(FlossmoleDataFile _rec) {
		isMoreRecentFile(new FileInfo(_rec.getUrl()));
	}
}
