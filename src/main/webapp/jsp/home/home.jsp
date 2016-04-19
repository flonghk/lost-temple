<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>lost-temple 平台</title>

<link href="themes/default/style.css" rel="stylesheet"	type="text/css" media="screen" />
<link href="themes/css/core.css" rel="stylesheet" type="text/css"	media="screen" />
<link href="themes/css/print.css" rel="stylesheet" type="text/css"	media="print" />
<link href="uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen" />
<link href="css/defined.css" rel="stylesheet" type="text/css" media="screen" />

<script src="js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="js/jquery.cookie.js" type="text/javascript"></script>
<script src="js/jquery.validate.js" type="text/javascript"></script>
<script src="js/jquery.bgiframe.js" type="text/javascript"></script>
<script src="xheditor/xheditor-1.2.1.min.js" type="text/javascript"></script>
<script src="xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
<script src="uploadify/scripts/jquery.uploadify.min.js" type="text/javascript"></script>
<script src="js/defined.js" type="text/javascript"></script>

<script src="bin/dwz.min.js" type="text/javascript"></script>
<script src="js/dwz.navTab.js" type="text/javascript"></script>
<script src="js/dwz.regional.zh.js" type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		DWZ.init("themes/dwz.frag.xml", {
			loginUrl : "login_dialog",
			loginTitle : "登录", // 弹出登录对话框
			statusCode : {
				ok : 200,
				error : 300,
				timeout : 301
			}, 
			pageInfo : {
				pageNum : "pageNum",
				numPerPage : "numPerPage",
				orderField : "orderField",
				orderDirection : "orderDirection"
			}, 
			debug : false, // 调试模式 【true|false】
			callback : function() {
				initEnv();
				$("#themeList").theme({
					themeBase : "themes"
				});
			}
		});
	});
	function clickRunningLog(){
		new LoadRunningLog();
	}
</script>
</head>

<body scroll="no">
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<a class="logo" href="#">标志</a>
				<ul class="nav">					
				</ul>
				<ul class="themeList" id="themeList">
					<li theme="default"><div class="selected">蓝色</div></li>
					<li theme="green"><div>绿色</div></li>
					<li theme="purple"><div>紫色</div></li>
					<li theme="silver"><div>银色</div></li>
					<li theme="azure"><div>天蓝</div></li>
				</ul>
			</div>
		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse">
						<div></div>
					</div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse">
					<h2>主菜单</h2>
					<div>收缩</div>
				</div>
				<div class="accordion" fillSpace="sidebar">
					<div class="accordionHeader"><h2><span>Folder</span>配置管理</h2></div>
					<div class="accordionContent">
						<ul class="tree">	
							<li><a href="config/list" target="navTab" mask="true" rel="configlist" title="config.xml">config.xml</a></li>
							<li><a href="component/list" target="navTab" mask="true" rel="componentlist" title="component.xml">component.xml</a></li>
						</ul>
					</div>	
					<div class="accordionHeader"><h2><span>Folder</span>数据管理</h2></div>
					<div class="accordionContent">
						<ul class="tree">	
							<li><a href="testdata/global/list" target="navTab" mask="true" rel="globaldatalist" title="global.xml">global.xml</a></li>
							<li><a href="testdata/list" target="navTab" mask="true" rel="testdatalist" title="测试数据">测试数据</a></li>
						</ul>
					</div>	
					<div class="accordionHeader"><h2><span>Folder</span>用例管理</h2></div>
					<div class="accordionContent">
						<ul class="tree">	
							<li><a href="testcase/list" target="navTab" mask="true" rel="testcaselist" title="测试用例">测试用例</a></li>
						</ul>
					</div>
					<div class="accordionHeader"><h2><span>Folder</span>运行管理</h2></div>
					<div class="accordionContent">
						<ul class="tree">	
							<li><a href="execute/running" target="navTab" mask="true" rel="runninglist" title="正在运行列表" onclick="clickRunningLog()">正在运行</a></li>
							<li><a href="execute/ran" target="navTab" mask="true" rel="ranlist" title="运行完成列表">运行完成</a></li>
						</ul>
					</div>						
				</div>
			</div>
		</div>

		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent">
						<!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">我的主页</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">我的主页</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="accountInfo">
							<div class="center">
								<p>
									<span style="color: red">lost-temple 平台</span>
								</p>
							</div>
						</div>
						<div class="pageFormContent" layoutH="80" style="margin-right: 230px">
							<p>
								<span>开发ing</span>
							</p>
							<div class="divider"></div>
							<p>
								<span>开发人员：张飞</span>									
							</p>
							<p>
								<span>网络ID：再见理想</span>									
							</p>
							<p>
								<span>QQ：408129370</span>									
							</p>
							<p>
								<span>博客：http://www.cnblogs.com/zhangfei/p/</span>									
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
