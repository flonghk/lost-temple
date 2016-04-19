package com.zf.common;

import com.zf.util.PropertiesHandler;

public class ConstantsUtil {
	
	public static String getTestRootPath(){
		return PropertiesHandler.getProperty("test.root.path");
	}
	
	public static String getBin(){
		return PropertiesHandler.getProperty("bin");
	}
	
	public static String getConfig(){
		return PropertiesHandler.getProperty("config");
	}
	
	public static String getConfigxml(){
		return PropertiesHandler.getProperty("config.xml");
	}
	
	public static String getComponentxml(){
		return PropertiesHandler.getProperty("component.xml");
	}
	
	public static String getKeywordxml(){
		return PropertiesHandler.getProperty("keyword.xml");
	}
	
	public static String getLib(){
		return PropertiesHandler.getProperty("lib");
	}
	
	public static String getTestcase(){
		return PropertiesHandler.getProperty("test-case");
	}
	
	public static String getTestdata(){
		return PropertiesHandler.getProperty("test-data");
	}
	
	public static String getTestresult(){
		return PropertiesHandler.getProperty("test-result");
	}
	
	public static String getGlobalxml(){
		return PropertiesHandler.getProperty("global.xml");
	}
	
	public static String getSuffix(){
		return PropertiesHandler.getProperty("suffix");
	}
	
	public static String getResultSuffix(){
		return PropertiesHandler.getProperty("result-suffix");
	}
	
	public static String getTestmethodName(){
		return PropertiesHandler.getProperty("test.method.name");
	}
	
	public static String getDefaultClassName(){
		return PropertiesHandler.getProperty("default.class.name");
	}
	
	public static String getCommonClassName(){
		return PropertiesHandler.getProperty("common.class.name");
	}
	
	public static String getLogEncode(){
		return PropertiesHandler.getProperty("log.encode");
	}
	
}
