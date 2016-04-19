package com.zf.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zf.common.AbstractService;
import com.zf.common.ConstantsUtil;
import com.zf.common.GlobalConstant;
import com.zf.common.TestPagerForm;
import com.zf.controller.testcase.TestcaseUtil;
import com.zf.controller.testdata.TestdataUtil;
import com.zf.pojo.TestcaseDetailsInfo;
import com.zf.util.AjaxResponseUtil.AjaxResponse;
import com.zf.util.CreateXml;
import com.zf.util.ParseXml;
import com.zf.util.ScanFileUtils;

@Service("testcaseService")
public class TestcaseService extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ComponentService componentService;
	
	public List<File> getListFiles(TestPagerForm pagerForm){
		List<File> list;
		try{
			ScanFileUtils su = new ScanFileUtils();
			su.setPagerForm(pagerForm);
			su.setRootPath(ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getTestcase());
			su.setSuffix(ConstantsUtil.getSuffix());
			su.setExcludeFile(ConstantsUtil.getGlobalxml());
			list = su.getListFiles();
			pagerForm = su.getPagerForm();
			this.flushPageInfo(pagerForm, pagerForm.getTotalCount());
			logger.info("Get the files count: "+pagerForm.getTotalCount());
		}catch(Exception e){
			logger.error("Get the file error - {}", e);
			list = new ArrayList<File>(0);
		}		
		return list;
	} 
	
	public synchronized AjaxResponse updateTestcaseName(String xmlName, String name){
		AjaxResponse ret = new AjaxResponse();
		if(!xmlName.equals(name) && this.fileExist(name)){
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
			ret.setMessage("该文件已存在: "+name);
			return ret;
		}
		try{
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestcase() + File.separator + xmlName+ConstantsUtil.getSuffix();
			if(!xmlName.equals(name)){
				File file = new File(path);
				boolean flag = file.renameTo(new File(file.getParentFile().getAbsoluteFile()+File.separator + name + ConstantsUtil.getSuffix()));
				if(!flag){
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("修改  "+path+" 失败！");
					return ret;
				}
			}
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("修改  "+path+" 成功！");
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("testcaselist");
			ret.setRel("testcaselist");
			ret.setForwardUrl("testcase/list");
		}catch(Exception e){
			logger.info("update test case {} to {} error - {}", xmlName, name, e);
			throw new RuntimeException(e);
		}
		return ret;
	}	
	
	public synchronized AjaxResponse addTestcaseName(String name){
		AjaxResponse ret = new AjaxResponse();
		if(this.fileExist(name)){
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
			ret.setMessage("该文件已存在: "+name);
			return ret;
		}
		try{
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestcase() + File.separator + name+ConstantsUtil.getSuffix();
			CreateXml cx = new CreateXml(path);
			Element rootElement = cx.addRootElement(TestdataUtil.ROOT);
			cx.addElement(rootElement, TestcaseUtil.ROOT);
			cx.saveXML();
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("增加  "+path+" 成功！");
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("testcaselist");
			ret.setRel("testcaselist");
			ret.setForwardUrl("testcase/list");
		}catch(Exception e){
			logger.info("add test case {} error - {}", name, e);
			throw new RuntimeException(e);
		}
		return ret;
	}	
	
	public synchronized boolean fileExist(String name){
		try{
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestcase() + File.separator + name+ConstantsUtil.getSuffix();
			File file = new File(path);
			return file.exists();
		}catch(Exception e){
			logger.info("check file {} exist error - {}", name, e);
			return false;
		}
	}
	
	public TestcaseDetailsInfo getTestcaseDetails(String name){
		try{
			String path = ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getTestcase()+File.separator+name+ConstantsUtil.getSuffix();
			ParseXml px = new ParseXml(path);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<String> listName = px.getChildrenNameByPath("/*");
			List<Map<String, Object>> allComp = componentService.getAllComponent();
			Map<String, String> allKeyword = componentService.getAllKeyword();
			List<String> classNames = new ArrayList<String>();
			for (Map<String, Object> map : allComp) {
				classNames.add(map.get("name").toString());
			}
			String lastClassName = ConstantsUtil.getDefaultClassName();
			int currentClassIndex = 0;
			for (int i = 0; i < listName.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String nodeName = px.getElementName(px.getElementObject("/*/*["+(i+1)+"]"));
				map.put("name", nodeName);
				map.put("keyword", allKeyword.containsKey(nodeName)?allKeyword.get(nodeName):nodeName);
				map.put("text", px.getElementText(px.getElementObject("/*/*["+(i+1)+"]")));
				Map<String, String> attr = px.getElementAttribute(px.getElementObject("/*/*["+(i+1)+"]"));
				if(attr.size()>0){
					map.put("rv", attr.get("return"));
				}else{
					map.put("rv", "");
				}
				if(classNames.contains(nodeName)){
					lastClassName = nodeName;
					map.put("isClass", 1);
					if(i!=0){
						currentClassIndex++;
					}
				}else{
					map.put("isClass", 0);
				}
				map.put("className", lastClassName);
				map.put("classIndex", currentClassIndex);
				map.put("methods", this.getTestMethodByClass(allComp, lastClassName, allKeyword));
				list.add(map);
			}
			TestcaseDetailsInfo info = new TestcaseDetailsInfo();
			info.setLastClassName(lastClassName);
			info.setLastClassIndex(currentClassIndex);
			info.setList(list);
			return info;
		}catch(Exception e){
			logger.info("test data error - {}", e);
			return new TestcaseDetailsInfo();
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	public Map<String, List<Map<String, String>>> getTestClassAndMethodByName(String className){
		Map<String, List<Map<String, String>>> allObject = new HashMap<String, List<Map<String, String>>>();
		try{
			List<Map<String, Object>> allComp = componentService.getAllComponent();
			Map<String, String> allKeyword = componentService.getAllKeyword();
			List<Map<String, String>> listClazz = new ArrayList<Map<String, String>>();
			List<Map<String, String>> listMethod = new ArrayList<Map<String, String>>();
			List<String> listCommon = new ArrayList<String>();
			List<String> listData = new ArrayList<String>();
			for (Map<String, Object> map : allComp) {
				String clazzName = map.get("name").toString();
				if(!clazzName.equals(ConstantsUtil.getCommonClassName())){
					Map<String, String> temp = new HashMap<String, String>();
					temp.put("name", clazzName);
					temp.put("keyword", allKeyword.containsKey(clazzName)?allKeyword.get(clazzName):clazzName);
					listClazz.add(temp);
				}
				if(clazzName.equals(ConstantsUtil.getCommonClassName())){
					listCommon = (List<String>) map.get("method");
				}
				if(clazzName.equals(className)){
					listData = (List<String>) map.get("method");
				}
			}
			listData.addAll(listCommon);
			for (String s : listData) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("name", s);
				temp.put("keyword", allKeyword.containsKey(s)?allKeyword.get(s):s);
				listMethod.add(temp);
			}
			allObject.put("clazz", listClazz);
			allObject.put("method", listMethod);
			return allObject;
		}catch(Exception e){
			logger.info("get test class error - {}", e);
			return allObject;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getTestMethodByClass(List<Map<String, Object>> allComp, String className, Map<String, String> allKeyword){
		List<Map<String, String>> allKey = new ArrayList<Map<String, String>>();
		try{
			List<String> listCommon = new ArrayList<String>();
			List<String> listData = new ArrayList<String>();
			for (Map<String, Object> map : allComp) {
				if(map.get("name").equals(ConstantsUtil.getCommonClassName())){
					listCommon = (List<String>) map.get("method");
				}
				if(map.get("name").equals(className)){
					listData = (List<String>) map.get("method");
				}
			}
			List<String> all = new ArrayList<String>();
			all.addAll(listCommon);
			all.addAll(listData);
			for (String s : all) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("name", s);
				temp.put("keyword", allKeyword.containsKey(s)?allKeyword.get(s):s);
				allKey.add(temp);
			}
			return allKey;
		}catch(Exception e){
			logger.info("get test method error - {}", e);
			return allKey;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getTestMethodByClass(String className){
		List<Map<String, String>> allKey = new ArrayList<Map<String, String>>();
		try{
			List<Map<String, Object>> allComp = componentService.getAllComponent();
			Map<String, String> allKeyword = componentService.getAllKeyword();
			List<String> listCommon = new ArrayList<String>();
			List<String> listData = new ArrayList<String>();
			for (Map<String, Object> map : allComp) {
				if(map.get("name").equals(ConstantsUtil.getCommonClassName())){
					listCommon = (List<String>) map.get("method");
				}
				if(map.get("name").equals(className)){
					listData = (List<String>) map.get("method");
				}
			}
			List<String> all = new ArrayList<String>();
			all.addAll(listCommon);
			all.addAll(listData);
			for (String s : all) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("name", s);
				temp.put("keyword", allKeyword.containsKey(s)?allKeyword.get(s):s);
				allKey.add(temp);
			}
			return allKey;
		}catch(Exception e){
			logger.info("get test method error - {}", e);
			return allKey;
		}
	}
		
	public synchronized AjaxResponse updateTestcaseXml(String name, List<Map<String, String>> list){
		AjaxResponse ret = new AjaxResponse();
		try{			
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestcase() + File.separator + name+ConstantsUtil.getSuffix();
			CreateXml cx = new CreateXml(path);
			Element rootElement = cx.addRootElement(TestcaseUtil.ROOT);
			for (Map<String, String> map : list) {
				String nodeName = map.get("name");
				boolean p = map.containsKey("param");
				boolean r = map.containsKey("rev");
				if(!p && !r){
					cx.addElement(rootElement, nodeName);
				}else if(p && !r){
					cx.addElement(rootElement, nodeName, map.get("param"));
				}else if(!p && r){
					Map<String, String> temp = new HashMap<String, String>();
					temp.put("return", map.get("rev"));
					cx.addElement(rootElement, nodeName, temp);
				}else if(p && r){
					Map<String, String> temp = new HashMap<String, String>();
					temp.put("return", map.get("rev"));
					cx.addElement(rootElement, nodeName, map.get("param"), temp);
				}
			}
			cx.saveXML();
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("修改  "+path+" 成功！");
			//ret.setCallbackType("closeCurrent");
			ret.setNavTabId("testcasedetails");
			ret.setCallbackType("");
//			ret.setForwardUrl("");
			//ret.setRel("testcasedetails");
			ret.setForwardUrl("testcase/details/"+name+ConstantsUtil.getSuffix());
		}catch(Exception e){
			logger.info("update test case - {} xml error - {}", name, e);
			throw new RuntimeException(e);
		}
		return ret;
	}	 
}
