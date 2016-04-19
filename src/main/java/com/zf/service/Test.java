package com.zf.service;

import java.util.ArrayList;
import java.util.List;

public class Test {
	
	public void test(){
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		list.add("e");
		list.add("f");
		List<String> slist = list.subList(list.size(), list.size());
		System.out.println(slist.toString());
		System.out.println(list.toString());
	}
	
	public static void main(String[] args) {
		System.out.println(boolean.class);
	}
	
}
