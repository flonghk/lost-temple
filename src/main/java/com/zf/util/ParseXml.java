package com.zf.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseXml {	
	/**
	 * 解析xml文件，我们需要知道xml文件的路径，然后根据其路径加载xml文件后，生成一个Document的对象，
	 * 于是我们先定义两个变量String filePath,Document document
	 * 然后再定义一个load方法，这个方法用来加载xml文件，从而产生document对象。
	 */
	private String filePath;

	private Document document; 
	
	/**
	 * 	构造器用来new ParseXml对象时，传一个filePath的参数进来,从而初始化filePath的值
	 * 调用load方法，从而在ParseXml对象产生时，就会产生一个document的对象。
	 */
	public ParseXml(String filePath) {		
		this.filePath = filePath;
		this.load(this.filePath);
	}
	
	/**
	 * 用来加载xml文件，并且产生一个document的对象
	 */
	private void load(String filePath){
		File file = new File(filePath);
		if (file.exists()) {
			SAXReader saxReader = new SAXReader();
			try {
				document = saxReader.read(file);
			} catch (DocumentException e) {		
				throw new RuntimeException("文件加载异常：" + filePath);				
			}
		} else{
			throw new RuntimeException("文件不存在 : " + filePath);
		}			
	}
	
	/**
	 * 用xpath来得到一个元素节点对象
	 * @param elementPath elementPath是一个xpath路径,比如"/config/driver"
	 * @return 返回的是一个节点Element对象
	 */
	public Element getElementObject(String elementPath) {
		return (Element) document.selectSingleNode(elementPath);
	}	
	
	@SuppressWarnings("unchecked")
	public List<Element> getElementObjects(String elementPath) {
		return document.selectNodes(elementPath);
	}
	
	public int getElementObjectsCount(String elementPath) {
		return document.selectNodes(elementPath).size();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getChildrenInfoByElement(Element element){
		Map<String, String> map = new HashMap<String, String>();
		List<Element> children = element.elements();
		for (Element e : children) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getChildrenInfoByPath(String elementPath){
		Element element = this.getElementObject(elementPath);
		Map<String, String> map = new HashMap<String, String>();
		List<Element> children = element.elements();
		for (Element e : children) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getChildrenInfoByElement(String elementPath){
		Element element = this.getElementObject(elementPath);
		List<Element> children = element.elements();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Element e : children) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("key", e.getName());
			map.put("value", e.getText());
			list.add(map);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getChildrenNameByPath(String elementPath){
		Element element = this.getElementObject(elementPath);
		List<Element> children = element.elements();
		List<String> list = new ArrayList<String>();
		for (Element e : children) {
			list.add(e.getName());
		}
		return list;
	}
	
	public int getChildrenSizeByPath(String elementPath){
		Element element = this.getElementObject(elementPath);
		return element.elements().size();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getElementAttribute(Element element){
		Map<String, String> map = new HashMap<String, String>();
		List<Attribute> attrs = element.attributes();
		for (Attribute attr : attrs) {
			map.put(attr.getName(), attr.getValue());
		}
		return map;
	}
	
	/**
	 * 用xpath来判断一个结点对象是否存在
	 */
	public boolean isExist(String elementPath){
		boolean flag = false;
		Element element = this.getElementObject(elementPath);
		if(element != null) flag = true;
		return flag;
	}
	
	/**
	 * 用xpath来取得一个结点对象的值
	 */
	public String getElementText(String elementPath) {
		Element element = this.getElementObject(elementPath);
		if(element != null){
			return element.getText();
		}else{
			return null;
		}		
	}
	
	public String getElementText(Element element){
		return element.getText();
	}
	
	public String getElementName(Element element){
		return element.getName();
	}
	
	public String getElementName(String elementPath){
		Element element = this.getElementObject(elementPath);
		if(element != null){
			return element.getName();
		}else{
			return null;
		}	
	}
	
	public static void main(String[] args) {
//		ParseXml px = new ParseXml("config/config.xml");//给定config.xml的路径
//		String browser = px.getElementText("/*/browser");
//		Log.logInfo(browser);
//		String waitTime = px.getElementText("/config/waitTime");
//		Log.logInfo(waitTime);
		
		ParseXml px = new ParseXml("test-case/global.xml");//给定config.xml的路径
		List<Element> elements = px.getElementObjects("/*/testUI");
		Object[][] object = new Object[elements.size()][];
		for (int i =0; i<elements.size(); i++) {
			Object[] temp = new Object[]{px.getChildrenInfoByElement(elements.get(i))};
			object[i] = temp;
		}
	}
	
}