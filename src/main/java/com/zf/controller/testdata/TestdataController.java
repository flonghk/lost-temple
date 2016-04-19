package com.zf.controller.testdata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zf.common.AbstractController;
import com.zf.common.ConstantsUtil;
import com.zf.common.TestPagerForm;
import com.zf.service.TestdataService;
import com.zf.util.AjaxResponseUtil;
import com.zf.util.AjaxResponseUtil.AjaxResponse;

@Controller
@RequestMapping(value="/testdata")
public class TestdataController extends AbstractController {
	
	@Autowired
	private TestdataService testdataService;
		
	@RequestMapping(value="/global/list")
	public String listGlobaldata(Map<String, Object> map) {	
		List<Map<String, String>> list = testdataService.getAllGlobal();	
		map.put("list", list);
		map.put("size", list.size());
		return "/jsp/test-data/globaldata.jsp";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/global/save")
	public void saveConfig(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String body = request.getParameter("data");
			Map<String, String> map = AjaxResponseUtil.loadObjectFromJsonString(body, Map.class);
			ret = testdataService.updateGlobalXml(map);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	
	@RequestMapping(value="/list")
	public String listTestdata(TestPagerForm pagerForm, Map<String, Object> map) {	
		List<File> listFiles = testdataService.getListFiles(pagerForm);	
		List<String> list = new ArrayList<String>();
		for (File file : listFiles) {
			list.add(file.getName());
		}
		map.put("list", list);
		map.put("pagerForm", pagerForm);
		return "/jsp/test-data/testdata_list.jsp";
	}
	
	@RequestMapping(value="/update/{name:.*}")
	public String updateTestdata(@PathVariable("name") String name, Map<String, Object> map){
		String fileName = name.substring(0, name.indexOf(ConstantsUtil.getSuffix()));
		map.put("entity", fileName);
		return "/jsp/test-data/testdata_update.jsp";
	}
	
	@RequestMapping(value="/update")
	public void updateTestdata(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String xmlName = request.getParameter("xmlname");
			String name = request.getParameter("name");
			ret = testdataService.updateTestdataName(xmlName, name);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/add")
	public String addTestdata(){
		return "/jsp/test-data/testdata_add.jsp";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public void addTestdata(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String name = request.getParameter("name");
			ret = testdataService.addTestdataName(name);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/details/{name:.*}")
	public String testdataDetails(@PathVariable("name") String name, Map<String, Object> map) {
		String fileName = name.substring(0, name.indexOf(ConstantsUtil.getSuffix()));
		map.put("name", fileName);
		List<Map<String, Object>> list = testdataService.getTestdataDetails(fileName);
		map.put("list", list);
		map.put("testmethod", ConstantsUtil.getTestmethodName());
		return "/jsp/test-data/testdata_details.jsp";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public void saveTestdata(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String name = request.getParameter("name");
			String body = request.getParameter("data");
			List<Map<String, Object>> list = AjaxResponseUtil.loadObjectFromJsonString(body, List.class);
			ret = testdataService.updateTestdataXml(name, list);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/json/{name:.*}/{table}/{tr}")
	public String testdataJson(@PathVariable("name") String name, @PathVariable("table") String table, @PathVariable("tr") String tr, Map<String, Object> map) {
		map.put("name", name);
		map.put("table", table);
		map.put("tr", tr);
		return "/jsp/test-data/testdata_json.jsp";
	}

}
