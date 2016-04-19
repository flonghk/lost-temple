package com.zf.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateXml {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String filePath;
	
	private Document document;
	
	public CreateXml(String filePath) {		
		this.filePath = filePath;
		this.document();
	}
	
	public CreateXml(String filePath, Document document) {		
		this.filePath = filePath;
		this.document = document;
	}
	
	public void document(){
		this.document = DocumentHelper.createDocument();
	}
	
	public Element addRootElement(String root){
		return document.addElement(root);
	}
	
	public Element addElement(Element parent, String name){
		Element element = parent.addElement(name);
		return element;
	}
	
	public Element addElement(Element parent, String name, String text){
		Element element = parent.addElement(name);
		if(text!=null){
			element.setText(text);
		}
		return element;
	}
	
	public Element addElement(Element parent, String name, Map<String, String> attr){
		Element element = parent.addElement(name);
		if(attr!=null){
			Set<String> set = attr.keySet();
			for (String k : set) {
				element.addAttribute(k, attr.get(k));
			}
		}
		return element;
	}
	
	public Element addElement(Element parent, String name, String text, Map<String, String> attr){
		Element element = parent.addElement(name);
		if(text!=null){
			element.setText(text);
		}
		if(attr!=null){
			Set<String> set = attr.keySet();
			for (String k : set) {
				element.addAttribute(k, attr.get(k));
			}
		}
		return element;
	}
	
	public void saveXML() {
		XMLWriter writer;
		try {				
			OutputFormat format = OutputFormat.createPrettyPrint();
			writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8"), format);
			writer.write(document);
			writer.close();
			logger.info("save "+filePath+" success!");
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("save "+filePath+" error! {}", e.toString());
		}
	}
	
}
