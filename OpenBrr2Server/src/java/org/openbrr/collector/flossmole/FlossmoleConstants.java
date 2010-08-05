package org.openbrr.collector.flossmole;

import java.text.SimpleDateFormat;

/**
 * 
 * @author Admin
 * Specify all the Flossmole constants in this file. 
 * BASE_URL - the URL where all the data files are present
 * DATA_FOLDER - the folder on the local server where the unprocessed and the processed files are stored. 
 *
 */

public interface FlossmoleConstants {

	String BASE_URL = "http://code.google.com/p/flossmole/downloads/list";
	String DOWNLOAD_BASE_URL = "http://flossmole.googlecode.com/files/";
	String DATA_FOLDER = "/Users/Admin/Desktop/OpenBrr/OpenBrr2Server/data/collectors/flossmole";
	String UNPROCESSED_FOLDER = DATA_FOLDER + "/files/unprocessed";
	String PROCESSED_FOLDER = DATA_FOLDER + "/files/processed";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MMM_dd-HHmmss");
}
