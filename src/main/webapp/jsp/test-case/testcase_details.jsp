<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>

<style type="text/css">
	.testcasedetails .addUnit1{
		width:5%;
	}
	.testcasedetails .addUnit2{
		width:25%;
	}
	.testcasedetails .addUnit3{
		width:30%;
	}
	.testcasedetails .addUnit4{
		width:20%;
	}
	.testcasedetails .addUnit5{
		width:20%;
	}
</style>

<script type="text/javascript">

	function getKeyword(clazz, name){
		var keyword = "";
		$.each(clazz, function(i,n){
			if(n.name==name){
				keyword = n.keyword;
				return false;
			}
		});
		return keyword;
	}

	function getAllObjectCallback(contentId, tbodyId, className, classIndex, isInsert){
		return function(json){
			DWZ.ajaxDone(json);		
			var info = JSON.parse(json);
			var str = "";
			if(info.statusCode == "200") {	
				var clazz = info.data.clazz;
				var methods = info.data.method;
				str = "<option value=\"\"></option>";
				str = str + "<option value=\"\" disabled>可用的类对象:</option>";
				$.each(clazz, function(i, n){
					str = str + "<option value=\""+n.name+"\" isclass=\"1\">"+n.keyword+"</option>";
				});
				var lastValue = $("#"+contentId+" .lastClassName").val();
				str = str + "<option value=\"\" disabled>可用的【"+getKeyword(clazz,lastValue)+"】类方法:</option>";
				$.each(methods, function(i, n){
					str = str + "<option value=\""+n.name+"\" isclass=\"0\">"+n.keyword+"</option>";
				});
			}
			if(isInsert==0){ //0 is add, 1 is insert
				$("#"+contentId+" #"+tbodyId+" select[ref='"+className+"'][refindex='"+classIndex+"']").last().append(str);
			}
			if(isInsert==1){
				$("#"+contentId+" #"+tbodyId+" tr.selected").prev().first().find("select").first().append(str);
			}
		}
	}
	
	function getAllObject(contentId, tbodyId){
		var className = $("#"+contentId+" .lastClassName").val();
		var classIndex = $("#"+contentId+" .lastClassIndex").val();
		addCaseRow(contentId, tbodyId, className, classIndex);
		$.post("testcase/all-obj",{"classname":$("#"+contentId+" .lastClassName").val()}, getAllObjectCallback(contentId, tbodyId, className, classIndex, 0));
	}
	
	function selectObject(contentId, tbodyId, obj){
		var selected = $(obj).val();
		if(selected==""){
			return;
		}
		var isclass = $(obj).children("option:selected").attr("isclass");
		var sel = $(obj).parent().first();
		if(isclass=="1"){
			$(obj).attr("isclass", "1");
			$(obj).attr("ref", selected);
			var refIndex = parseInt($("#"+contentId+" .lastClassIndex").val())+1;
			$(obj).attr("refindex", refIndex);
			$(obj).attr("disabled", true);
			$("#"+contentId+" .lastClassName").val(selected);
			$("#"+contentId+" .lastClassIndex").val(refIndex);
		}else{
			$(obj).attr("isclass", "0");
			$(obj).removeAttr("onchange");
			$(obj).children("option[value=\"\"]").remove();
			$(obj).children("option[isclass=\"1\"]").remove();
		}
	}
	
	function getFistClassName(contentId, trObjs){
		var className = $("#"+contentId+" .defaultClassName").val();
		var classIndex = 0;
		var map = new Object();
		map["cn"] = className;
		map["ci"] = classIndex;
		$.each(trObjs, function(i, n){
			var selObj = $(n).find("select").first();
			if($(selObj).attr("isclass")=="1"){
				className = $(selObj).attr("ref");
				classIndex = $(selObj).attr("refindex");
				map["cn"] = className;
				map["ci"] = classIndex;
				return false;
			}
		});
		return map;
	}
	
	function getAllMethodCallback(contentId, tbodyId, className, classIndex){
		return function(json){
			DWZ.ajaxDone(json);		
			var info = JSON.parse(json);
			var str = "";
			if(info.statusCode == "200") {	
				var methods = info.data;
				str = "<option value=\"\"></option>";
				$.each(methods, function(i, n){
					str = str + "<option value=\""+n.name+"\" isclass=\"0\">"+n.keyword+"</option>";
				});
			}
			$("#"+contentId+" #"+tbodyId+" tr.selected").prev().first().find("select").first().append(str);
		}
	}
	
	function insertObject(contentId, tbodyId){
		var selectedRow = $("#"+contentId+" #"+tbodyId+" tr.selected");
		if(selectedRow.length==0){
			return;
		}
		var sel = $(selectedRow).first().find("select").first();
		var isclass = $(sel).attr("isclass");
		var className = $(sel).attr("ref");
		var selectedPrevRow = $(selectedRow).prevAll();			
		var map = getFistClassName(contentId, selectedPrevRow);
		var className = map["cn"];
		var classIndex = map["ci"];
		if(isclass=="0"){
			insertCaseRow(contentId, tbodyId, className, classIndex);
			$.post("testcase/all-method",{"classname":className}, getAllMethodCallback(contentId, tbodyId, className, classIndex));
		}
		if(isclass=="1"){
			insertCaseRow(contentId, tbodyId, className, classIndex);
			$.post("testcase/all-obj",{"classname":className}, getAllObjectCallback(contentId, tbodyId, className, classIndex, 1));
		}
	}
	
	function saveAllTestcase(){
		var alldata = new Array();
		var contentId = "testcasedetails_${name}";
		var tbodyId = "testcasedetailstable_${name}";
		var flag = checkKey(contentId, tbodyId);
		if(flag){
			var json = generateTestcaseJson();
			$.post("testcase/save",{"name":"${name}","data":json}, saveCaseXmlCallback);
		}else{
			alertMsg.error("key不能为空或不能为数字！");
		}
	}
	
	function generateTestcaseJson(){
		var json = new Array(); 
		var index = 0;
		$("#testcasedetails_${name} #testcasedetailstable_${name} tr").each(function(){
			var key = {}; 
			var inputs = $(this).find(".xmlinput");
			key["name"] = $(inputs[0]).val();
			var p = $(inputs[1]).val();
			if(p != ""){
				key["param"] = p;
			}
			var r = $(inputs[2]).val();
			if(r != ""){
				key["rev"] = r;
			}
			json[index] = key;
			index++;
		});
		return JSON.stringify(json); 
	}

</script>

<div id="testcasedetails_${name}" class="pageContent testcasedetails">	
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="#" onclick="getAllObject('testcasedetails_${name}', 'testcasedetailstable_${name}')"><span>新增</span></a></li>
			<li><a class="add" href="#" onclick="insertObject('testcasedetails_${name}', 'testcasedetailstable_${name}')"><span>插入</span></a></li>
			<li><a class="add" href="#" onclick="saveAllTestcase()"><span>保存</span></a></li>
			<li><a class="add" href="testdata/details/${name}.xml" mask="true" rel="testdatadetails" target="navtab" title="测试数据详情"><span>脚本数据</span></a></li>
		</ul>
		<input class="size" type="hidden" value="${size}">
		<input class="testName" type="hidden" value="${name}">
		<input class="lastClassName" type="hidden" value="${lastClassName}">
		<input class="defaultClassName" type="hidden" value="${defaultClassName}">
		<input class="lastClassIndex" type="hidden" value="${lastClassIndex}">
	</div>
	<table class="table" width="100%" layoutH="110" nowrapTD="false" >
		<thead>
			<tr>
				<th class="addUnit1">step</th>
				<th class="addUnit2">action</th>	
				<th class="addUnit3">参数</th>	
				<th class="addUnit4">返回值</th>
				<th class="addUnit5">操作</th>	
			</tr>
		</thead>
		<tbody id="testcasedetailstable_${name}">
 			<c:forEach var="item" items="${list}" varStatus="index">
				<tr id="${index.count}" onclick="selectedRowCss('testcasedetails_${name}', 'testcasedetailstable_${name}',this)">
					<td><c:out value="${index.count}" /></td>
					<%-- <td><input class="xmlinput" type="text" value="${item.name}"></td> --%>
					<td>
						<select class="xmlinput" ref="${item.className}" refindex="${item.classIndex}" isclass="${item.isClass}" <c:if test="${item.isClass==1}">disabled</c:if>>
							<c:if test="${item.isClass==1}">
								<option value="${item.name}">${item.keyword}</option>
							</c:if>
							<c:if test="${item.isClass==0}">
								<c:forEach var="it" items="${item.methods}">
									<option value="${it.name}" <c:if test="${it.name==item.name}">selected</c:if>>${it.keyword}</option>
								</c:forEach>
							</c:if>
						</select>
					</td>
					<td><input class="xmlinput" type="text" value="<c:out value="${item.text}"/>" onmouseover="blurRes(this)" onmouseout="unblurRes('testcasedetails_${name}',this)" onfocus="focusRes('testcasedetails_${name}',this)"></td>
					<td><input class="xmlinput" type="text" value="<c:out value="${item.rv}"/>"></td>
					<td>
                    	<a class="btnDel" href="#" onclick="deleteCaseRow('testcasedetails_${name}', this)">删除</a>     
                	</td>
				</tr> 			
			</c:forEach>
		</tbody>
	</table>
	<div id="suggest" onmouseover="removeBlur('testcasedetails_${name}')" onmouseout="addBlur('testcasedetails_${name}')" style="left: 0px; top: 0px; display: none;">
		<ul>
			<c:forEach var="it" items="${paramData}" varStatus="index">
				<li onclick="selectedSuggest('testcasedetails_${name}','testcasedetailstable_${name}',this)"><c:out value="${it}"/></li>
			</c:forEach>
		</ul>
	</div>
</div>

