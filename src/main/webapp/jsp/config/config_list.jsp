<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>

<style type="text/css">
	#config .addUnit1{
		width:5%;
	}
	#config .addUnit2{
		width:30%;
	}
	#config .addUnit3{
		width:30%;
	}
	#config .addUnit4{
		width:30%;
	}
</style>



<div id="config" class="pageContent">	
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="#" onclick="addRow('config', 'configTable')"><span>新增</span></a></li>
			<li><a class="add" href="#" onclick="saveXml('config', 'configTable', 'config/save')"><span>保存</span></a></li>
		</ul>
		<input class="size" type="hidden" value="${size}">
	</div>
	<table class="table" width="100%" layoutH="110" nowrapTD="false" >
		<thead>
			<tr>
				<th class="addUnit1">序号</th>
				<th class="addUnit2">KEY</th>	
				<th class="addUnit3">VALUE</th>	
				<th class="addUnit4">操作</th>	
			</tr>
		</thead>
		<tbody id="configTable">
 			<c:forEach var="item" items="${list}" varStatus="index">
				<tr id="${index.count}">
					<td><c:out value="${index.count}" /></td>
					<td><input class="xmlinput" type="text" value="<c:out value="${item.key}" />"></td>
					<td><input class="xmlinput" type="text" value="<c:out value="${item.value}" />"></td>
					<td>
                    	<a class="btnDel" href="#" onclick="deleteRow('config', '${index.count}')">删除</a>     
                	</td>
				</tr> 			
			</c:forEach>
		</tbody>
	</table>
</div>

