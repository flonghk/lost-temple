<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<script type="text/javascript">

	/* function loadLogCallback(json){
		DWZ.ajaxDone(json);		
		var info = JSON.parse(json);
		if(info.statusCode == "200") {
			$("#runninglog_${name} #log_${name}").html("<div>"+info.data+"</div>");
		}else if(info.statusCode == "201"){
			$("#runninglog_${name} #log_${name}").html("<div>"+info.data+"</div>");
			window.clearInterval(log);
			//eval("window.clearInterval(log${name});");
		}else{
			window.clearInterval(log);
			//eval("window.clearInterval(log${name});");
			alertMsg.error(info.message);
		}
	}
	
	function loadLog(){
		$.post("execute/look",{"name":$("#runninglog_${name} #h_${name}").val()}, loadLogCallback);
	}	
	var log = window.setInterval(loadLog,1000);
	//eval("var log${name} = window.setInterval(loadLog,1000);"); */
	
	$(function(){
		new LoadLogObject("${name}");
	});
	
</script>
<div class="accountInfo">
	<div><p></p><p>【${name}】运行LOG：</p></div>
</div>
<div class="pageContent" id="runninglog_${name}">	
	<input type="hidden" id="h_${name}" value="${name}">
	<input type="hidden" id="index_${name}" value="0">
	<div style="width: 98%; overflow: auto;" layouth="80" id="log_${name}">
		
	</div>
</div>


