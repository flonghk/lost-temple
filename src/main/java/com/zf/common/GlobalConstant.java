package com.zf.common;



public final class GlobalConstant {
	
	/** session 里属性的 key 值 */
	public static final class SessionKey {
		/** 登录用户信息在 session 里的 key 值 */
		public static final String LOGIN_USER = "login_user";
	}
	
	
	
	/** Ajax 返回的状态码 */
	public static final class AjaxResponseStatusCode {
		/** 成功 */
		public static final String SUCCESS = "200";
		/** 失败 */
		public static final String FAIL = "300";
		/** 登录失效 */
		public static final String TIMEOUT = "301";
		/** 提示 */
		public static final String CONFIRM = "302";
	}
	
	/** 排序方向 */
	public static final class OrderDirection {
		/** 升序 */
		public static final String ASC = "asc";
		/** 降序 */
		public static final String DESC = "desc";
	}
	
	
	
	
}
