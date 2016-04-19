package com.zf.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zf.common.AbstractService;
import com.zf.common.ConstantsUtil;
import com.zf.common.GlobalConstant;
import com.zf.common.TestPagerForm;
import com.zf.controller.executor.ExecutorUtil;
import com.zf.pojo.ReportInfo;
import com.zf.thread.ThreadItem;
import com.zf.thread.ThreadPoolManager;
import com.zf.util.AjaxResponseUtil;
import com.zf.util.AjaxResponseUtil.AjaxResponse;
import com.zf.util.ScanFileUtils;

@Service("executorService")
public class ExecutorService extends AbstractService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ThreadPoolManager threadPoolManager;
	
	public synchronized AjaxResponse execute(String names){
		AjaxResponse ret = new AjaxResponse();
		try{
			long ln = this.getLocalName();
			ThreadItem ti = new ThreadItem(ln, names);
			threadPoolManager.enqueue(ti);
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("等待执行："+ln);
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("testcaselist");
			ret.setRel("testcaselist");
			ret.setForwardUrl("testcase/list");
		}catch(Exception e){
			logger.info("execute test case {} error - {}", names, e);
			throw new RuntimeException(e);
		}
		return ret;
	}	
	
	public synchronized long getLocalName(){
		long name = System.currentTimeMillis();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return name;
	}	
	
	@SuppressWarnings("unchecked")
	public AjaxResponse lookLog(long name, int index){
		AjaxResponse ret = new AjaxResponse();
		try{
			if(!ExecutorUtil.getLog().containsKey(name)){
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
				ret.setData("该senario: "+name+" 已经执行完毕！");
//				ret.setCallbackType("closeCurrent");
//				ret.setNavTabId("runninglist");
//				ret.setRel("runninglist");
//				ret.setForwardUrl("execute/running");
				return ret;
			}
			
			List<String> log = (List<String>) ExecutorUtil.getLog().get(name).get(ExecutorUtil.L_MSG);
			List<String> sLog = new ArrayList<String>();
			if(index<=log.size()){
				sLog = log.subList(index, log.size());
			}
			if(!Boolean.valueOf((Boolean) ExecutorUtil.getLog().get(name).get(ExecutorUtil.L_STATUS))){
				//ExecutorUtil.del(name);
				ret.setStatusCode("201");
				ret.setData(sLog);
				return ret;
			}
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setData(sLog);
		}catch(Exception e){
			logger.info("look log {} error - {}", name, e);
			throw new RuntimeException(e);
		}
		return ret;
	}
	
	public List<File> getListFiles(TestPagerForm pagerForm){
		List<File> list;
		try{
			ScanFileUtils su = new ScanFileUtils();
			su.setPagerForm(pagerForm);
			su.setRootPath(ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getTestresult());
			su.setSuffix(ConstantsUtil.getResultSuffix());
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
	
	public List<ReportInfo> readLogFile(String name){
		try{
			List<ReportInfo> list = AjaxResponseUtil.loadObjectFromJsonString(this.read(name), new TypeReference<List<ReportInfo>>(){});			
			return list;
		}catch(Exception e){
			logger.info("get log {} error - {}", name, e);
			return new ArrayList<ReportInfo>(0);
		}
	}
	
	private String read(String name){
		BufferedReader br = null;
		InputStreamReader is = null;
		FileInputStream fis = null;
		StringBuffer sb = new StringBuffer();
		try {
			fis = new FileInputStream(ConstantsUtil.getTestRootPath()+File.separator+ConstantsUtil.getTestresult()+File.separator+name);
			is = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(is); 
			String line = null; 
			while ((line = br.readLine()) != null){
				 sb.append(line);
			}
			logger.info("load the log file success! - {}", name);
			return sb.toString();
		} catch (IOException e) {			
			logger.error("load the log file - {} error! - {}",name, e);
			return null;
		}finally{			
			try {
				if(fis != null){
					fis.close();
				}
				if(is != null){
					is.close();
				}
				if(br != null){
					br.close();
				}
			} catch (IOException e) {					
				logger.error("close the log file - {} error! - {}", name, e);
			}			
		}
	}
}
