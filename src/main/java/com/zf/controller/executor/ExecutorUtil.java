package com.zf.controller.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExecutorUtil {
	
	private static ConcurrentMap<Long, Map<String, Object>> LOG = new ConcurrentHashMap<Long, Map<String, Object>>();
	
	public static ConcurrentMap<Long, Integer> ALLLOG = new ConcurrentHashMap<Long, Integer>();
	
	public static String L_MSG = "log";
	
	public static String L_STATUS = "status";
	
	public static String BEGIN_TIMER = "timer";
	
	@SuppressWarnings("unchecked")
	public synchronized static void add(long name, String log, boolean status){
		Map<String, Object> temp = LOG.get(name);
		if(temp==null){
			temp = new HashMap<String, Object>();
		}
		List<String> list = new	ArrayList<String>();
		if(temp.containsKey(ExecutorUtil.L_MSG)){
			list = (List<String>) temp.get(ExecutorUtil.L_MSG);
		}
		if(log!=null){
			list.add(log);
		}
		temp.put(ExecutorUtil.L_MSG, list);
		temp.put(ExecutorUtil.L_STATUS, status);
		LOG.put(name, temp);
	}
	
	public synchronized static ConcurrentMap<Long, Map<String, Object>> getLog(){
		return LOG;
	}
	
	public synchronized static void del(long name){
		LOG.remove(name);
	}
	
	public synchronized static boolean existTimer(long name){
		return LOG.get(name).containsKey(BEGIN_TIMER);
	} 
	
	public synchronized static void addTimer(long name){
		LOG.get(name).put(BEGIN_TIMER, true);
	} 
	
	public synchronized static void del(){
		Set<Long> keys = LOG.keySet();
		for (Long key : keys) {
			if(!Boolean.valueOf((Boolean) LOG.get(key).get(ExecutorUtil.L_STATUS))){
				LOG.remove(key);
			}
		}
	}
	
	public synchronized static String get(long name){
		try{
			return LOG.get(name).get(L_MSG).toString();
		}catch(Exception e){
			return null;
		}
	}
	
}
