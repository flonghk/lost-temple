<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<script type="text/javascript">

	function keywordJson(){
		var key = {};  
		$("#component tr").each(function(){
			var inputs = $(this).children();
			var input = $(inputs[1]).find(".xmlinput").first();
			if($.trim($(input).val())!=""){
				key[$.trim($(inputs[0]).text())] = $.trim($(input).val());
			}
		});
		return JSON.stringify(key); 
	}
	
	function saveAllKeyword(){
		var alldata = new Array();
		var json = keywordJson();
		$.post("component/save",{"data":json}, saveXmlCallback);
	}
</script>
<style type="text/css">
	
</style>

<div class="pageContent">
	<div id="component" class="pageFormContent" layoutH="30">
		<div class="panelBar">
			<ul class="toolBar">
				<li><a class="add" href="component/generate" target="ajaxTodo" title="重新生成组件"><span>重新生成组件</span></a></li>
				<li><a class="add" href="#" onclick="saveAllKeyword()"><span>保存</span></a></li>
			</ul>
		</div>
		<c:forEach var="item" items="${list}">
			<div class="unitBox" style="float:left; display:block; overflow:auto; width:30%; padding:0px 1px">
				<div class="pageContent" style="border-left:1px #B8D0D6 solid;border-right:1px #B8D0D6 solid">
					<div class="panelBar">
						<ul class="toolBar">					
						</ul>
					</div>
					<table class="table" width="100%" layoutH="300">
						<thead>
							<tr>
								<th style="width:50%;font-weight:bold"><c:out value="${item.name}" /></th>						
								<th style="width:50%;font-weight:bold"><input class="xmlinput" value="<c:out value="${keyword[item.name]}" />"></th>
							</tr>
						</thead>
						<tbody>
				 			<c:forEach var="it" items="${item.method}">
								<tr>
									<td><c:out value="${it}" /></td>
									<td><input class="xmlinput" value="<c:out value="${keyword[it]}" />"></td>
								</tr> 			
							</c:forEach>
						</tbody>
					</table>
					<div class="panelBar">
						<div class="pages">
							<span>共${item.size}条</span>
						</div>				
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>

