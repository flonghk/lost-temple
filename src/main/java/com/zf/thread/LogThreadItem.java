package com.zf.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import com.zf.common.ConstantsUtil;
import com.zf.controller.executor.ExecutorUtil;

public class LogThreadItem implements Callable<Object>{
	
	private InputStream is;
	
	private long name;

	public LogThreadItem(InputStream is, long name) {
		this.is = is;
		this.name = name;
	}

	public Object call() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(is, ConstantsUtil.getLogEncode()));
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line != null) {
					ExecutorUtil.add(name, line , true);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
