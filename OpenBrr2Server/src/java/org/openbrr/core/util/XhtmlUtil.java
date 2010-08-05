package org.openbrr.core.util;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Node;

/**
 * Utility class used by the Crawler to download the files from the Flossmole URL
 * 
 * 
*/
public class XhtmlUtil {

	public static String getNodeData(Node _node) {
		String data = null;
		if (_node != null && _node.getFirstChild() != null) {
			data = _node.getFirstChild().getNodeValue();
		}
		return data;
	}

	public static void downloadFile(String _url, String _toFile) {
		URL url;
		InputStream in = null;
		FileOutputStream out = null;
		try {
			url = new URL(_url);
			URLConnection con = url.openConnection();

			in = new BufferedInputStream(con.getInputStream());
			System.out.println("InputStream: " + in);
			// IOUtil.writeStreamToFile(in, _toFile);
			// *
			in = new BufferedInputStream(in);
			out = new FileOutputStream(_toFile);

			int i = 0;
			byte[] bytesIn = new byte[1024];
			while ((i = in.read(bytesIn)) >= 0) {
				out.write(bytesIn, 0, i);
			}
			// */
			System.out.println("File downloaded");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtil.closeStream(in);
			IOUtil.closeStream(out);
		}

	}

	/**
	 * 
	 * @param args
	 * just to test the class 
	 * 
	 */
	
	public static void main(String[] args) {
		String url = "http://flossmole.googlecode.com/files/sfRawStatusData2008-Oct.txt.bz2";
		String toFile = "/home/ashutosh/tmp/sfRawStatusData2008-Oct.txt.bz2";
		downloadFile(url, toFile);
		IOUtil.uncompressFilesInFolder("/home/ashutosh/tmp");
	}

}
