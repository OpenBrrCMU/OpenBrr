package org.openbrr.collector.flossmole.crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openbrr.collector.flossmole.PROJECT_CODE;

public class FileInfo {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM");
	private static String VALID_FILE_EXT = ".txt.bz2";
	
	private PROJECT_CODE projectCode;
	private String fileName;
	private String fileType;
	private String fileUrl;
	private Date fileDate;
	private String parseError;
	
	public FileInfo(String _url) {
		fileUrl = _url;
		analyzeUrl();
	}
	
	public void analyzeUrl() {
		
		fileName = fileUrl.substring(fileUrl.lastIndexOf('/')+1);
		System.out.println("\tFilename: "+fileName);
		
		//check for valid projects
		for(PROJECT_CODE code : PROJECT_CODE.values()) {
			if(fileName.startsWith(code.toString())) {
				projectCode = code;
				break;
			}
		}
		
		System.out.println("\tProjectCode: "+projectCode);
		
		if(projectCode == null) {
			parseError = "Error: Unsupported Project";
			return;
		}
		
		/*
		 * Check for valid file extension
		 */
		if(!fileName.endsWith(VALID_FILE_EXT)) {
			parseError = "Error: Unrecognized format.";
			return;
		}
		
		/*
		 * find date:
		 * 
		 * All fileNameas have the naming pattern:
		 * 	-	sfRawRealUrlData2008-Jun.txt.bz2
		 * 
		 * The code tracks the date comp around '-' character
		 */
		int dashPos = fileName.indexOf('-');
		String dateStr = fileName.substring(dashPos - 4, dashPos + 4);
		
		System.out.println("\tdateStr: "+dateStr);
		try {
			fileDate = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			parseError = "Error: Cannot parse date";
			return;
		}
		
		/*
		 * fileType = fileName - projectCode
		 */
		fileType = fileName.substring(projectCode.toString().length(), fileName.indexOf('.')-8);
		System.out.println("\tfileType: "+fileType);
		
		/*
		 * find ext; to check
		 */
		//fileExt = fileName.substring(fileName.indexOf('.'));
		
	}

	public PROJECT_CODE getProjectCode() {
		return projectCode;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public String getParseError() {
		return parseError;
	}
	
	public boolean hasParseError() {
		return parseError != null;
	}
	
	public void setStatus(String _str) {
		parseError = _str;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() 
			+"\n\t projectCode: "+projectCode
			+"\n\t fileType: "+fileType
			+"\n\t fileUrl: "+fileUrl
			+"\n\t fileDate: "+fileDate
			+"\n\t error: "+parseError;
	}

	//for testing
	public static void main(String[] args) {
		String[] urlList = {
			"http://flossmole.googlecode.com/files/sfRawRealUrlData2008-Jun.txt.bz2",
			"http://flossmole.googlecode.com/files/rfProjectTopic2008-Jul.txt.bz2",
			"http://flossmole.googlecode.com/files/datamart_sf_other.2008-Aug.sql.new.1.bz2",
			"http://flossmole.googlecode.com/files/rfProjectTopic2008-Jun.txt.bz2",
		};
		
		for(String url : urlList) {
			System.out.println("Processing URL: "+url);
			FileInfo info = new FileInfo(url);
			if(info.hasParseError()) {
				System.out.println("Parsing failed: "+info.getParseError());
			} else {
				System.out.println("Parsing Success: "+info);
			}
		}
	}
	
}
