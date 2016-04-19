package com.zf.controller.testcase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zf.common.AbstractController;
import com.zf.common.ConstantsUtil;
import com.zf.common.GlobalConstant;
import com.zf.common.TestPagerForm;
import com.zf.pojo.TestcaseDetailsInfo;
import com.zf.service.ExecutorService;
import com.zf.service.TestcaseService;
import com.zf.service.TestdataService;
import com.zf.util.AjaxResponseUtil;
import com.zf.util.AjaxResponseUtil.AjaxResponse;

@Controller
@RequestMapping(value="/testcase")
public class TestcaseController extends AbstractController {
	
	@Autowired
	private TestcaseService testcaseService;
	
	@Autowired
	private TestdataService testdataService;
	
	@Autowired
	private ExecutorService executorService;
	
	@RequestMapping(value="/list")
	public String listTestcase(TestPagerForm pagerForm, Map<String, Object> map) {	
		List<File> listFiles = testcaseService.getListFiles(pagerForm);	
		List<String> list = new ArrayList<String>();
		for (File file : listFiles) {
			list.add(file.getName());
		}
		map.put("list", list);
		map.put("pagerForm", pagerForm);
		return "/jsp/test-case/testcase_list.jsp";
	}
	
	@RequestMapping(value="/update/{name:.*}")
	public String updateTestcase(@PathVariable("name") String name, Map<String, Object> map){
		String fileName = name.substring(0, name.indexOf(ConstantsUtil.getSuffix()));
		map.put("entity", fileName);
		return "/jsp/test-case/testcase_update.jsp";
	}
	
	@RequestMapping(value="/update")
	public void updateTestcase(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String xmlName = request.getParameter("xmlname");
			String name = request.getParameter("name");
			ret = testcaseService.updateTestcaseName(xmlName, name);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/add")
	public String addTestdata(){
		return "/jsp/test-case/testcase_add.jsp";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public void addTestcase(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String name = request.getParameter("name");
			ret = testcaseService.addTestcaseName(name);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/details/{name:.*}")
	public String testcaseDetails(@PathVariable("name") String name, Map<String, Object> map) {
		String fileName = name.substring(0, name.indexOf(ConstantsUtil.getSuffix()));
		map.put("name", fileName);
		TestcaseDetailsInfo info = testcaseService.getTestcaseDetails(fileName);
		List<String> paramData = testdataService.getAllData(fileName);
		map.put("list", info.getList());
		map.put("size", info.getList().size());
		map.put("lastClassName", info.getLastClassName());
		map.put("lastClassIndex", info.getLastClassIndex());
		map.put("defaultClassName", ConstantsUtil.getDefaultClassName());
		map.put("paramData", paramData);
		return "/jsp/test-case/testcase_details.jsp";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public void saveTestcase(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String name = request.getParameter("name");
			String body = request.getParameter("data");
			List<Map<String, String>> list = AjaxResponseUtil.loadObjectFromJsonString(body, List.class);
			ret = testcaseService.updateTestcaseXml(name, list);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/all-obj")
	public void getAllObject(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String className = request.getParameter("classname");
			Map<String, List<Map<String, String>>> map = testcaseService.getTestClassAndMethodByName(className);
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setData(map);
		} catch (Exception e) {
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/all-method")
	public void getAllMethod(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String className = request.getParameter("classname");
			List<Map<String, String>> list = testcaseService.getTestMethodByClass(className);
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setData(list);
		} catch (Exception e) {
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/all-param")
	public void getAllParam(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String testName = request.getParameter("testname");
			String returnParam = request.getParameter("returnparam");
			List<String> list = AjaxResponseUtil.loadObjectFromJsonString(returnParam, List.class);
			List<String> listData = testdataService.getAllData(testName);
			Set<String> set = new HashSet<String>();
			set.addAll(list);
			set.addAll(listData);
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setData(new ArrayList<String>(set));
		} catch (Exception e) {
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
}
