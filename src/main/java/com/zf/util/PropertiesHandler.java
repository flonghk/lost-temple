package com.zf.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesHandler.class);
	
	private static Properties p = new Properties();
	
	static{
		try {
			InputStream in = PropertiesHandler.class.getClassLoader().getResourceAsStream("application.properties");
			p.load(in);
			in.close();
		} catch (IOException e) {
			throw new RuntimeException("application.properties load error!");
		}
	}
	
	public static String getProperty(String key){
		try{
			return p.getProperty(key, null);
		}catch(Exception e){
			logger.info("please check the property - {}, - {}");
		}
		return "";
	}
	
}
