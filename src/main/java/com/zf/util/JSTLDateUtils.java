package com.zf.util;
/**
 * 自定义标签，用于long转换成时间
 */
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class JSTLDateUtils extends TagSupport {
	
	private static final long serialVersionUID = -3354015192721342312L;
	private String value;
	private String parttern;

	public void setValue(String value) {
		this.value = value;
	}
	public void setParttern(String parttern) {
		this.parttern = parttern;
	}

	public int doStartTag() throws JspException {
		long time = Long.parseLong(value);
		Date date = new Date(time);
		SimpleDateFormat dateformat = new SimpleDateFormat(parttern);
		String s = dateformat.format(date.getTime());
		try {
			pageContext.getOut().write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
