package com.zf.controller.executor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zf.common.AbstractController;
import com.zf.common.TestPagerForm;
import com.zf.pojo.ReportInfo;
import com.zf.service.ExecutorService;
import com.zf.util.AjaxResponseUtil;
import com.zf.util.AjaxResponseUtil.AjaxResponse;

@Controller
@RequestMapping(value="/execute")
public class ExecutorController extends AbstractController {
	
	@Autowired
	private ExecutorService executorService;
	
	@RequestMapping(value="/senario", method=RequestMethod.POST)
	public void runTestcase(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String names = request.getParameter("names");
			ret = executorService.execute(names);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	
	@RequestMapping(value="/running")
	public String listRunning(TestPagerForm pagerForm, Map<String, Object> map) {	
		ConcurrentMap<Long, Integer> logs = ExecutorUtil.ALLLOG;
		/*List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Set<Long> set = logs.keySet();
		for (Long l : set) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("name", l);
			temp.put(ExecutorUtil.L_MSG, logs.get(l).get(ExecutorUtil.L_MSG));
			temp.put(ExecutorUtil.L_STATUS, logs.get(l).get(ExecutorUtil.L_STATUS));
			list.add(temp);
		}*/
		Map<Long, String> allLog = new HashMap<Long, String>();
		Set<Long> set = logs.keySet();
		for (Long l : set) {
			String status = "";
			switch(logs.get(l)){
				case 0: status="等待运行";break;
				case 1: status="正在运行";break;
				case 2: status="运行完成";break;
			}
			allLog.put(l, status);
		}
		map.put("list", allLog);
		map.put("pagerForm", pagerForm);
		return "/jsp/test-run/running_list.jsp";
	}
	
	@RequestMapping(value="/running/{name:.*}")
	public String listRunningGet(@PathVariable("name") Long name, Map<String, Object> map) {	
		map.put("name", name);
		return "/jsp/test-run/running_log.jsp";
	}
	
	@RequestMapping(value="/look", method=RequestMethod.POST)
	public void goRunning(HttpServletRequest request, HttpServletResponse response){
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		
		try {
			long name = Long.valueOf(request.getParameter("name"));
			int index = Integer.valueOf(request.getParameter("index"));
			ret = executorService.lookLog(name, index);
		} catch (Exception e) {
			//this.genAjaxErrorInfo(ret, e);
			ret.setStatusCode("202");
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/ran")
	public String listTestLog(TestPagerForm pagerForm, Map<String, Object> map) {	
		List<File> listFiles = executorService.getListFiles(pagerForm);	
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try{
			for (File file : listFiles) {
				String[] fileNames = file.getName().split("\\.");
				String fn = fileNames[0];
				String pass = fileNames[1];
				String fail = fileNames[2];
				String skip = fileNames[3];
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("name", fn);
				temp.put("pass", pass);
				temp.put("fail", fail);
				temp.put("skip", skip);
				temp.put("all", file.getName());
				list.add(temp);
			}
		}catch(Exception e){
			logger.error("get test log error - {}",e);
		}
		map.put("list", list);
		map.put("pagerForm", pagerForm);
		return "/jsp/test-run/ran_list.jsp";
	}
	
	@RequestMapping(value="/ran/{name:.*}")
	public String listRanGet(@PathVariable("name") String name, Map<String, Object> map) {
		String fileName = name.split("\\.")[0];
		List<ReportInfo> list = executorService.readLogFile(name);
		map.put("name", fileName);
		map.put("list", list);
		return "/jsp/test-run/ran_log.jsp";
	}
	
}
