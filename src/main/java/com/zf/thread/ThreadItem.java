package com.zf.thread;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import com.zf.common.ConstantsUtil;
import com.zf.controller.executor.ExecutorUtil;

public class ThreadItem implements Callable<Object> {
	
	private String tcs;

	private long name;

	public ThreadItem(long name, String tcs) {
		this.name = name;
		this.tcs = tcs;
	}

	public long getName() {
		return name;
	}

	public Object call() throws Exception {
		this.executor();
		return null;
	}

	private void executor() throws IOException {
		ExecutorUtil.add(name, "begin to execut: "+name, true);
		ExecutorUtil.ALLLOG.put(name, 1);
		String rootPath = ConstantsUtil.getTestRootPath();
		String libPath = rootPath + File.separator + ConstantsUtil.getLib();
		String binPath = rootPath + File.separator + ConstantsUtil.getBin();
		String command = "java -cp " + binPath + " -Djava.ext.dirs=" + libPath + " com.zf.run.Run " + tcs.replaceAll(ConstantsUtil.getSuffix(), "")+" "+name+" "+rootPath;
		final Process process = Runtime.getRuntime().exec(command);
		final InputStream[] iss = new InputStream[]{process.getInputStream(), process.getErrorStream()};
		LogThreadPool ltp = new LogThreadPool(iss, process, name);
		ltp.threadPoolControl();
//		try {
//			Thread.sleep(20000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		new Thread() {
//			public void run() {
//				LogThreadPool ltp = new LogThreadPool(iss, process, name);
//				ltp.threadPoolControl();
//			}
//		}.start();
	}

}
