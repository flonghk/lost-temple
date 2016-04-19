package com.zf.controller.config;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zf.common.AbstractController;
import com.zf.service.ConfigService;
import com.zf.util.AjaxResponseUtil;
import com.zf.util.AjaxResponseUtil.AjaxResponse;

@Controller
@RequestMapping(value="/config")
public class ConfigController extends AbstractController {
	
	@Autowired
	private ConfigService configService;
		
	@RequestMapping(value="/list")
	public String listConfig(Map<String, Object> map) {	
		List<Map<String, String>> list = configService.getAllConfig();	
		map.put("list", list);
		map.put("size", list.size());
		return "/jsp/config/config_list.jsp";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/save")
	public void saveConfig(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String body = request.getParameter("data");
			Map<String, String> map = AjaxResponseUtil.loadObjectFromJsonString(body, Map.class);
			ret = configService.updateConfigXml(map);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	
	
	

}
