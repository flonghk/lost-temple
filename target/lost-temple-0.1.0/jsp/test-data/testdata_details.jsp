<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>

<style type="text/css">
	.testdatadetails .addUnit1{
		width:5%;
	}
	.testdatadetails .addUnit2{
		width:40%;
	}
	.testdatadetails .addUnit3{
		width:40%;
	}
	.testdatadetails .addUnit4{
		width:15%;
	}
	
	
</style>

<script type="text/javascript">
	function saveAllData(){
		var alldata = new Array(); 
		var flag = true;
		$(".testdatadetails div[name='testdatadetails']").each(function(i){
			var contentId = $(this).attr("id");
			var index = $(this).attr("index");
			var tbodyId = "testdatadetailstable_"+index;
			flag = checkKey(contentId, tbodyId);
			if(flag){
				var json = generateJson(contentId, tbodyId);
				var testdata = new Object(); 
				testdata.name=$("#"+contentId+" .iscommon").val();
				testdata.data=json;
				alldata[i]=testdata;
			}else{
				return false;
			}
		});
		if(flag){
			var data = JSON.stringify(alldata);
			$.post("testdata/save",{"name":$(".testdatadetails #name").val(),"data":data}, saveXmlCallback);
		}else{
			alertMsg.error("key不能为空或不能为数字！");
		}
	}
	
	function delTable(obj){
		$(obj).parentsUntil("div.unitBox").remove();
	}
	
</script>

<div class="pageContent testdatadetails">
	<div id="testdatadetails_${name}" class="pageFormContent" layoutH="30">
		<div class="panelBar">
			<ul class="toolBar">
				<li><a class="add" href="#"><span>详情: ${name}</span></a></li>
				<li style="flat:right;"><a class="add" href="#" onclick="saveAllData()"><span>保存</span></a></li>
				<li style="flat:right;"><a class="add" href="#" onclick="addTable('${name}')"><span>新增测试数据</span></a></li>
			</ul>
			<input id="name" type="hidden" value="${name}">
			<input id="testmethod" type="hidden" value="${testmethod}">
			<input id="tableSize" type="hidden" value="${fn:length(list)}">
		</div>
		<c:forEach var="item" items="${list}" varStatus="index">
			<div class="unitBox" style="float:left; display:block; overflow:auto; width:30%; padding:0px 1px">
				<div class="pageContent" name="testdatadetails" index="${index.count}" id="testdatadetails_${name}_${index.count}" style="border-left:1px #B8D0D6 solid;border-right:1px #B8D0D6 solid">
					<div class="panelBar">
						<ul class="toolBar">
							<li><a class="icon" href="#"><span>
								<c:choose>  
								   <c:when test="${item.name=='common'}">
								   		<c:out value="${item.name}"></c:out>
								   </c:when>  
								   <c:otherwise>
								   		<c:out value="测试数据"></c:out>
								   </c:otherwise>  
								</c:choose> 
								</span></a></li>
							<li style="float:right;">
								<c:if test="${item.name!='common'}">
									<a class="delete" href="#" onclick="delTable(this)"><span>删除</span></a>
								</c:if>
								<a class="add" href="#" onclick="addDataRow('${name}', '${index.count}')"><span>新增</span></a>
							</li>
						</ul>
						<input class="size" type="hidden" value="${item.size}">
						<input class="iscommon" type="hidden" value="${item.name}">
					</div>
					<table class="table" width="100%" layoutH="300">
						<thead>
							<tr>
								<th class="addUnit1">序号</th>
								<th class="addUnit2">数据名</th>
								<th class="addUnit3">数据值</th>	
								<th class="addUnit4">操作</th>						
							</tr>
						</thead>
						<tbody id="testdatadetailstable_${index.count}">
				 			<c:forEach var="it" items="${item.param}" varStatus="in">
								<tr id="${in.count}" onclick="selectedRowCss('testdatadetails_${name}','testdatadetailstable_${index.count}',this)">
									<td class="addUnit1"><c:out value="${in.count}" /></td>
									<td class="addUnit2"><input class="xmlinput" type="text" value="<c:out value="${it.key}" />"></td>
									<td class="addUnit3">
										<input class="xmlinput" type="text" value="<c:out value="${it.value}" />">
										<a height="800" width="800" rel="jsonformatter" target="dialog" href="testdata/json/${name}/${index.count}/${in.count}" class="btnSelect">格式化JSON</a>
										<!-- <a class="btnSelect" href="#" onclick="jsonFormatter(this)">格式化JSON</a> -->
									</td>
									<td class="addUnit4">
				                    	<a class="btnDel" href="#" onclick="deleteRow('testdatadetails_${name}_${index.count}', '${in.count}')">删除</a>     
				                	</td>
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

