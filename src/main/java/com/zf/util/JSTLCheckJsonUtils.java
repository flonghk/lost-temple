package com.zf.util;
import net.sf.json.JSONArray;
/**
 * 自定义标签，用于long转换成时间
 */
import net.sf.json.JSONObject;

public class JSTLCheckJsonUtils {
	
	public static boolean isJson(String s) {
        boolean flag = false;
        if(JSTLCheckJsonUtils.isJsonObject(s) || JSTLCheckJsonUtils.isJsonArray(s)){
        	flag = true;
        }
        return flag;
    }
	
	private static boolean isJsonObject(String s) {
        boolean flag = true;
        try {
            JSONObject.fromObject(s);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
	
	private static boolean isJsonArray(String s) {
        boolean flag = true;
        try {
            JSONArray.fromObject(s);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
	
	public static void main(String[] args) {
		System.out.println(JSTLCheckJsonUtils.isJson("123"));
	}
	
}
