package com.zf.thread;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zf.controller.executor.ExecutorUtil;

@Component("delMapLogThread")
public class DelMapLogThread implements Runnable{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PostConstruct
	public void delLog() {
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		while(true){
			try{
				ConcurrentMap<Long, Map<String, Object>> log = ExecutorUtil.getLog();
				Set<Long> keys = log.keySet();
				for (final Long key : keys) {
					if(!Boolean.valueOf((Boolean) log.get(key).get(ExecutorUtil.L_STATUS)) && !ExecutorUtil.existTimer(key)){
						new Thread(){
							@SuppressWarnings("static-access")
							public void run(){
								ExecutorUtil.addTimer(key);
								try {
									Thread.currentThread().sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								ExecutorUtil.del(key);
								ExecutorUtil.ALLLOG.remove(key);
							}
						}.start();
					}
				}
				//ExecutorUtil.del();
				Thread.sleep(2000);
			} catch (Exception e) {
				logger.error("delete log error.  {}", e.getMessage());
			}
		}
	}
	
	
	
	
}
