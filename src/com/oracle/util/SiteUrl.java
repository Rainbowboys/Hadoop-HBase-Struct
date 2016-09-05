package com.oracle.util;

import java.io.FileInputStream;
import java.util.Properties;

import com.oracle.servlet.InitServlet;

public class SiteUrl {
	static String path = null;
	static Properties properties = new Properties();

	static {
		try {
			FileInputStream fis;
			path = InitServlet.class.getResource("/").getPath().toString()
					.trim();

			fis = new FileInputStream(path + "conf.properties");
		
			properties.load(fis);
			fis.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	 
	public static String readUrl(String key){
		String url=properties.getProperty(key);
		return url;
	}
}
