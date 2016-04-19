package com.zf.thread;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zf.controller.executor.ExecutorUtil;

public class LogThreadPool {
	
	private InputStream[] iss = new InputStream[2];
	
	private long name;
	
	private Process process;

	private int threadPoolSize = 2;

	private ExecutorService service;

	public LogThreadPool(InputStream[] iss, Process process, long name) {
		this.iss = iss;
		this.process = process;
		this.name = name;
	}

	public void threadPoolControl() {
		service = Executors.newFixedThreadPool(threadPoolSize);
		Collection<LogThreadItem> c = new ArrayList<LogThreadItem>();
		for (int i = 0; i < iss.length; i++) {
			c.add(new LogThreadItem(iss[i], name));
		}
		try {
			service.invokeAll(c);
			service.shutdown();
			process.destroy();
			ExecutorUtil.add(name, null, false);
			ExecutorUtil.ALLLOG.put(name, 2);
			//ExecutorUtil.del();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
