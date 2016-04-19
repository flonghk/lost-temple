package com.zf.common;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.util.AjaxResponseUtil;
import com.zf.util.AjaxResponseUtil.AjaxResponse;

public abstract class AbstractController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 产生错误信息
	 */
	protected void genAjaxErrorInfo(AjaxResponse ret, Exception e) {
		ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
		String uuid = UUID.randomUUID().toString();
		logger.error("ERROR_ID:" + uuid, e);
		ret.setMessage("发生内部错误<br>ERROR_ID:" + uuid + "<br>请与管理员联系");
	}
	
	/**
	 * 返回前端 Ajax 请求的结果
	 */
	protected void responseAjaxJson(HttpServletResponse response, AjaxResponse ret){
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(AjaxResponseUtil.getAjaxJson(ret));
			response.getWriter().flush();
		} catch (Exception e) {
			logger.error("Fail to response ajax json.", e);
		}
	}	
}
