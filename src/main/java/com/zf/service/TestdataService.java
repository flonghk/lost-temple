package com.zf.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zf.common.AbstractService;
import com.zf.common.ConstantsUtil;
import com.zf.common.GlobalConstant;
import com.zf.common.TestPagerForm;
import com.zf.controller.testdata.TestdataUtil;
import com.zf.util.AjaxResponseUtil.AjaxResponse;
import com.zf.util.AjaxResponseUtil;
import com.zf.util.CreateXml;
import com.zf.util.ParseXml;
import com.zf.util.ScanFileUtils;

@Service("testdataService")
public class TestdataService extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*@PostConstruct
	public void test(){
		System.out.println(PropertiesHandler.getProperty("test.root.path"));
	}*/
	
	public List<Map<String, String>> getAllGlobal(){
		String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestdata() + File.separator + ConstantsUtil.getGlobalxml();
		ParseXml px = new ParseXml(path);//给定config.xml的路径
		List<Map<String, String>> list = px.getChildrenInfoByElement("/*");
		logger.info("config data count - {}", list.size());
		return list;
	}
	
	public List<String> getAllData(String testName){
		try{
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestdata() + File.separator + ConstantsUtil.getGlobalxml();
			ParseXml px = new ParseXml(path);//给定config.xml的路径
			Set<String> listAll = new HashSet<String>();
			List<String> globalList = px.getChildrenNameByPath("/*");
			listAll.addAll(globalList);
			String dataPath = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestdata() + File.separator + testName+ConstantsUtil.getSuffix();
			File file = new File(dataPath);
			if(file.exists()){
				ParseXml p = new ParseXml(dataPath);
				int count =  p.getElementObjectsCount("/*/*");
				for (int i = 1; i <= count; i++) {
					List<String> dataList = p.getChildrenNameByPath("/*/*["+i+"]");
					listAll.addAll(dataList);
				}
			}
			return new ArrayList<String>(listAll);
		}catch(Exception e){
			logger.info("get all test data error - {}", e);
			return new ArrayList<String>(0);
		}
	}
	
	public synchronized AjaxResponse updateGlobalXml(Map<String, String> map){
		AjaxResponse ret = new AjaxResponse();
		try{			
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestdata() + File.separator + ConstantsUtil.getGlobalxml();
			ParseXml px = new ParseXml(path);
			String root = px.getElementName(px.getElementObject("/*"));
			CreateXml cx = new CreateXml(path);
			Element rootElement = cx.addRootElement(root);
			Set<String> set = map.keySet();
			for (String s : set) {
				cx.addElement(rootElement, s, map.get(s));
			}
			cx.saveXML();
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("修改  "+path+" 成功！");
			ret.setForwardUrl("testdata/global/list");
		}catch(Exception e){
			logger.info("add global xml error - {}", e);
			throw new RuntimeException(e);
		}
		return ret;
	}	 
	
	public List<File> getListFiles(TestPagerForm pagerForm){
		List<File> list;
		try{
			ScanFileUtils su = new ScanFileUtils();
			su.setPagerForm(pagerForm);
			su.setRootPath(ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getTestdata());
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
	
	public synchronized AjaxResponse updateTestdataName(String xmlName, String name){
		AjaxResponse ret = new AjaxResponse();
		if(!xmlName.equals(name) && this.fileExist(name)){
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
			ret.setMessage("该文件已存在: "+name);
			return ret;
		}
		try{
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestdata() + File.separator + xmlName+ConstantsUtil.getSuffix();
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
			ret.setNavTabId("testdatalist");
			ret.setRel("testdatalist");
			ret.setForwardUrl("testdata/list");
		}catch(Exception e){
			logger.info("update test data {} to {} error - {}", xmlName, name, e);
			throw new RuntimeException(e);
		}
		return ret;
	}	
	
	public synchronized AjaxResponse addTestdataName(String name){
		AjaxResponse ret = new AjaxResponse();
		if(this.fileExist(name)){
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
			ret.setMessage("该文件已存在: "+name);
			return ret;
		}
		try{
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestdata() + File.separator + name+ConstantsUtil.getSuffix();
			CreateXml cx = new CreateXml(path);
			Element rootElement = cx.addRootElement(TestdataUtil.ROOT);
			cx.addElement(rootElement, TestdataUtil.COMMON);
			cx.saveXML();
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("增加  "+path+" 成功！");
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("testdatalist");
			ret.setRel("testdatalist");
			ret.setForwardUrl("testdata/list");
		}catch(Exception e){
			logger.info("add test data {} error - {}", name, e);
			throw new RuntimeException(e);
		}
		return ret;
	}	
	
	public synchronized boolean fileExist(String name){
		try{
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestdata() + File.separator + name+ConstantsUtil.getSuffix();
			File file = new File(path);
			return file.exists();
		}catch(Exception e){
			logger.info("check file {} exist error - {}", name, e);
			return false;
		}
	}
	
	public List<Map<String, Object>> getTestdataDetails(String name){
		try{
			String path = ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getTestdata()+File.separator+name+ConstantsUtil.getSuffix();
			if(!this.fileExist(name)){
				CreateXml cx = new CreateXml(path);
				Element rootElement = cx.addRootElement(TestdataUtil.ROOT);
				cx.addElement(rootElement, TestdataUtil.COMMON);
				cx.saveXML();
			}
			ParseXml px = new ParseXml(path);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<String> listClassName = px.getChildrenNameByPath("/*");
			for (int i = 0; i < listClassName.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", listClassName.get(i));
				Map<String, String> params = px.getChildrenInfoByPath("/*/*["+(i+1)+"]");
				map.put("param", params);
				map.put("size", params.size());
				list.add(map);
			}
			return list;
		}catch(Exception e){
			logger.info("test data error - {}", e);
			return new ArrayList<Map<String, Object>>(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized AjaxResponse updateTestdataXml(String name, List<Map<String, Object>> list){
		AjaxResponse ret = new AjaxResponse();
		try{			
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getTestdata() + File.separator + name+ConstantsUtil.getSuffix();
			CreateXml cx = new CreateXml(path);
			Element rootElement = cx.addRootElement(TestdataUtil.ROOT);
			for (Map<String, Object> map : list) {
				String nodeName = (String) map.get("name");
				Element element = cx.addElement(rootElement, nodeName);
				Map<String, String> param = AjaxResponseUtil.loadObjectFromJsonString((String) map.get("data"), Map.class);
				Set<String> set = param.keySet();
				for (String s : set) {
					cx.addElement(element, s, param.get(s));
				}
			}
			cx.saveXML();
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("修改  "+path+" 成功！");
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("testdatalist");
			ret.setRel("testdatalist");
			ret.setForwardUrl("testdata/list");
		}catch(Exception e){
			logger.info("update test data xml error - {}", e);
			throw new RuntimeException(e);
		}
		return ret;
	}	 
}
