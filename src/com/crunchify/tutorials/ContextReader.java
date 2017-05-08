package com.crunchify.tutorials;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ContextReader {
	public static String URI;
	public static String pageToken;
	public static String getURI(){
		if(URI==null){ 
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			URI=prop.getProperty("mongoDBURI");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		}
		return URI;
	}
	
		public static String getPageToken(){
			if(pageToken==null){ 
			Properties prop = new Properties();
			InputStream input = null;

			try {

				input = new FileInputStream("config.properties");

				// load a properties file
				prop.load(input);

				// get the property value and print it out
				pageToken=prop.getProperty("pageToken");

			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			}
			return pageToken;
		}
		
	public static void main(String[] args) {
		System.out.println(getURI());
	}

}
