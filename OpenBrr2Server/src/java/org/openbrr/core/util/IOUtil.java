	package org.openbrr.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;

/**
 * 
 * @author Admin
 * This Utility class is mainly used to uncompress the files downloaded from the Flossmole URL
 */

public class IOUtil {

	public static void writeStreamToFile(InputStream _in, String _toFile) {
		BufferedInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new BufferedInputStream(_in);
			out = new FileOutputStream(_toFile);
			
			int i = 0;
			byte[] bytesIn = new byte[1024];
			while ((i = in.read(bytesIn)) >= 0) {
				out.write(bytesIn, 0, i);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtil.closeStream(out);
		}

	}

	public static void uncompressFilesInFolder(String _folder) {
		
		File folder = new File(_folder);
		if(!folder.exists()) {
			System.out.println("Error, no folder :"+_folder);
			return;
		}
		
		for(File file : folder.listFiles(new Bzip2Filter())) {
			String[] cmdArray = {"bunzip2", file.getName()};
			try {
				System.out.println("Unzipping "+file.getName());
				Runtime.getRuntime().exec(cmdArray, null, folder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void closeWriter(Writer _w) {
		try {
			if(_w != null) {
				_w.flush();
				_w.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void closeStream(OutputStream _stream) {
		try {
			if(_stream != null) {
				_stream.flush();
				_stream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closeStream(InputStream _stream) {
		try {
			if(_stream != null) {
				_stream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static class Bzip2Filter implements FilenameFilter {

		public boolean accept(File _folder, String _fileName) {
			return _fileName.endsWith(".bz2");
		}
		
	}
}
