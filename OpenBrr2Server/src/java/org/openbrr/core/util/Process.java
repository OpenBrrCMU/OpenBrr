package org.openbrr.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Process {
	
	private String[] cmdArray;
	private String[] env;
	private File baseFolder;
	
	public Process(String[] _cmd, String[] _env, File _folder) {
		cmdArray = _cmd;
		env = _env;
		baseFolder = _folder;
	}
	
	public int exec() {
		return exec(false);
	}
	
	public int exec(boolean _waitForCompletion) {
		
		int exitValue = 0;
		
		if(baseFolder != null && !baseFolder.exists()) {
			System.out.println("Cannot locate Folder: "+baseFolder);
			return -1;
		}
		
		
		java.lang.Process proc = null;
		try {
			proc = Runtime.getRuntime().exec(cmdArray, env, baseFolder);
			ProcReader out = new ProcReader("Output", proc.getInputStream());
			ProcReader error = new ProcReader("Error", proc.getErrorStream());
			
			Thread outT = new Thread(out);
			Thread errT = new Thread(error);
			
			outT.start();
			errT.start();
			
			if(_waitForCompletion) {
				outT.join();
				errT.join();
				
				proc.waitFor();
				exitValue = proc.exitValue();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return exitValue;
	}
	
	class ProcReader implements Runnable {
		private BufferedReader reader;
		private String label;
		
		public ProcReader(String _label, InputStream _is) {
			label = _label;
			reader = new BufferedReader(new InputStreamReader(_is));
		}
		
		public void start() throws IOException {
			while(reader.ready()) {
				String line = reader.readLine();
				System.out.println(label+": "+line);
			}
		}

		public void run() {
			try {
				start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
