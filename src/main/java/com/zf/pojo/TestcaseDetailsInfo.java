package com.zf.pojo;

import java.util.List;
import java.util.Map;

public class TestcaseDetailsInfo {
	
	private String lastClassName;
	
	private int lastClassIndex;
	
	private List<Map<String, Object>> list;

	public String getLastClassName() {
		return lastClassName;
	}

	public void setLastClassName(String lastClassName) {
		this.lastClassName = lastClassName;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public int getLastClassIndex() {
		return lastClassIndex;
	}

	public void setLastClassIndex(int lastClassIndex) {
		this.lastClassIndex = lastClassIndex;
	}
	
}
