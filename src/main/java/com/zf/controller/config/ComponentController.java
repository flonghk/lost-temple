package com.zf.controller.config;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zf.common.AbstractController;
import com.zf.service.ComponentService;
import com.zf.util.AjaxResponseUtil;
import com.zf.util.AjaxResponseUtil.AjaxResponse;

@Controller
@RequestMapping(value="/component")
public class ComponentController extends AbstractController {
	
	@Autowired
	private ComponentService componentService;
		
	@RequestMapping(value="/list")
	public String listComponent(Map<String, Object> map) {	
		List<Map<String, Object>> list = componentService.getAllComponent();	
		Map<String, String> keywordMap = componentService.getAllKeyword();
		map.put("list", list);
		map.put("keyword", keywordMap);
		return "/jsp/config/component_list.jsp";
	}
	
	@RequestMapping(value="/generate")
	public void generateComponent(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = componentService.generateComponent();
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/save")
	public void saveKeyword(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String body = request.getParameter("data");
			Map<String, String> map = AjaxResponseUtil.loadObjectFromJsonString(body, Map.class); 
			ret = componentService.saveKeyword(map);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}

}
