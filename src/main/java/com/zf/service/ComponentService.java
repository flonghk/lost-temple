package com.zf.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.zf.controller.config.ConfigUtil;
import com.zf.util.AjaxResponseUtil.AjaxResponse;
import com.zf.util.CreateXml;
import com.zf.util.ParseXml;

@Service("componentService")
public class ComponentService extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<Map<String, Object>> getAllComponent(){
		try{
			String path = ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getConfig()+File.separator+ConstantsUtil.getComponentxml();
			ParseXml px = new ParseXml(path);//给定config.xml的路径
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<String> listClassName = px.getChildrenNameByPath("/*");
			for (int i = 0; i < listClassName.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", listClassName.get(i));
				List<String> listMethodName = px.getChildrenNameByPath("/*/*["+(i+1)+"]");
				map.put("method", listMethodName);
				map.put("size", listMethodName.size());
				list.add(map);
			}
			logger.info("component data count - {}", list.size());
			return list;
		}catch(Exception e){
			logger.info("component data error - {}", e);
			throw new RuntimeException("component data error!");
		}
	}
	
	public Map<String, String> getAllKeyword(){
		try{
			String path = ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getConfig()+File.separator+ConstantsUtil.getKeywordxml();
			ParseXml px = new ParseXml(path);
			Map<String, String> map = new HashMap<String, String>();
			int len = px.getChildrenSizeByPath("/*");
			for (int i = 0; i < len; i++) {
				String xpath = "/*/*["+(i+1)+"]";
				map.put(px.getElementName(xpath), px.getElementText(xpath));
			}
			logger.info("keyword data count - {}", map.size());
			return map;
		}catch(Exception e){
			logger.info("keyword data error - {}", e);
			throw new RuntimeException("keyword data error!");
		}
	}
	
	public synchronized AjaxResponse generateComponent(){
		AjaxResponse ret = new AjaxResponse();
		try{			
			String rootPath = ConstantsUtil.getTestRootPath();
			String libPath = rootPath+File.separator+ConstantsUtil.getLib();
			String binPath = rootPath+File.separator+ConstantsUtil.getBin();
			String command = "java -cp "+binPath+" -Djava.ext.dirs="+libPath+" com.zf.annotation.GenerateComponentXML "+rootPath;
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
			process.destroy();
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("更新  component.xml 成功！");
			ret.setNavTabId("componentlist");
			ret.setRel("componentlist");
			ret.setForwardUrl("component/list");
		}catch(Exception e){
			logger.info("generate component xml error - {}", e);
			throw new RuntimeException(e);
		}
		return ret;
	}	
	
	public synchronized AjaxResponse saveKeyword(Map<String, String> map){
		AjaxResponse ret = new AjaxResponse();
		try{			
			String path = ConstantsUtil.getTestRootPath() + File.separator + ConstantsUtil.getConfig() + File.separator + ConstantsUtil.getKeywordxml();
			CreateXml cx = new CreateXml(path);
			Element rootElement = cx.addRootElement(ConfigUtil.ROOT);
			Set<String> keys = map.keySet();
			for (String key : keys) {
				if(map.get(key)!=null && !map.get(key).trim().equals("")){
					cx.addElement(rootElement, key, map.get(key));
				}
			}
			cx.saveXML();
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("修改  "+path+" 成功！");
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("componentlist");
			ret.setRel("componentlist");
			ret.setForwardUrl("component/list");
		}catch(Exception e){
			logger.info("save key word xml error - {}", e);
			throw new RuntimeException(e);
		}
		return ret;
	}	 
	
}
