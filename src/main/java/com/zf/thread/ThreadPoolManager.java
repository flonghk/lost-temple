package com.zf.thread;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zf.controller.executor.ExecutorUtil;

@Component("threadPoolManager")
public class ThreadPoolManager implements Runnable{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ConcurrentLinkedQueue<ThreadItem> localQueue = new ConcurrentLinkedQueue<ThreadItem>();
	
	private final int threadPoolSize = 2;
	
	private ExecutorService service;
	
	public boolean enqueue(ThreadItem ti) {
		return localQueue.add(ti);
	}
	
	@PostConstruct
	public void runQueueManager() {
		service = Executors.newFixedThreadPool(threadPoolSize);
		Thread threadManager = new Thread(this);
		threadManager.start();
	}

	public void run() {
		while(true){
			try{
				while(localQueue.peek() != null) {
					ThreadItem ti = localQueue.poll();
					ExecutorUtil.ALLLOG.put(ti.getName(), 0);
					service.submit(ti);
				}
				Thread.sleep(2000);
				//ExecutorUtil.del();
			} catch (Exception e) {
				logger.error("execute error.  {}", e.getMessage());
			}
		}
	}
	
	
	
	
}
