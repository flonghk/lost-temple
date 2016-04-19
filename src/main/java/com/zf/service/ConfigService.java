package com.zf.service;

import java.io.File;
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
import com.zf.util.AjaxResponseUtil.AjaxResponse;
import com.zf.util.CreateXml;
import com.zf.util.ParseXml;

@Service("configService")
public class ConfigService extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*@PostConstruct
	public void test(){
		System.out.println(PropertiesHandler.getProperty("test.root.path"));
	}*/
	
	public List<Map<String, String>> getAllConfig(){
		String path = ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getConfig()+File.separator+ConstantsUtil.getConfigxml();
		ParseXml px = new ParseXml(path);//给定config.xml的路径
		List<Map<String, String>> list = px.getChildrenInfoByElement("/*");
		logger.info("config data count - {}", list.size());
		return list;
	}
	
	public synchronized AjaxResponse updateConfigXml(Map<String, String> map){
		AjaxResponse ret = new AjaxResponse();
		try{			
			String path = ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getConfig()+File.separator+ConstantsUtil.getConfigxml();
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
			ret.setForwardUrl("config/list");
		}catch(Exception e){
			logger.info("add config xml error - {}", e);
			throw new RuntimeException(e);
		}
		return ret;
	}	 
	
	
}
