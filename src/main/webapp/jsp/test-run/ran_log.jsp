<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<script type="text/javascript">
	function logJsonFormatter(name){
		$("#ranlog_"+name+" textarea.ranlogjson").each(function(i){
			var v = $(this).val();
			v = getJsonString(v,'  ');
			$(this).val(v);
		});
	}
</script>

<style>
	.logfont{
		font-weight:bold;
		color:#00F;
	}
</style>

<div class="pageHeader">	
	<div class="searchBar">
		<span class="logfont">【${name}】 - 详细报告</span>
	</div>
</div>

<div class="pageContent" id="ranlog_${name}">	
	<div class="panelBar">
        <ul class="toolBar">
            <li><a class="add" href="#" onclick="logJsonFormatter('${name}')"><span>格式化JSON</span></a></li>
        </ul>
    </div>
	<table class="table" width="100%" layoutH="50" nowrapTD="false" >
		<thead>
			<tr>
				<th width="30%"></th>
				<th width="70%"></th>	
			</tr>
		</thead>
		<tbody>
 			<c:forEach var="item" items="${list}">
				<tr>
					<td class="logfont">用例名称:</td>
					<td><c:out value="${item.name}"/></td>
				</tr> 
				<tr>
					<td class="logfont">运行状态:</td>
					<td>${item.status}</td>
				</tr> 
				<tr>
					<td class="logfont">运行时间:</td>
					<td>${item.spendTime}毫秒</td>
				</tr>	
				<tr>
					<td colspan=2 class="logfont">参数:</td>
				</tr>
				<c:forEach var="it" items="${item.parameters}">
					<tr>
						<td>${it.key}:</td>
						<td><c:out value="${it.value}"/></td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan=2 class="logfont">LOG:</td>
				</tr>	
				<c:forEach var="it" items="${item.log}">
					<c:if test="${json:check(it)==true}">
						<tr style="height:100px;">
							<td colspan=2>
								 <textarea style="width:100%;height:100%" class="ranlogjson"><c:out value="${it}"/></textarea>
							</td>
						</tr>
					</c:if>
					<c:if test="${json:check(it)==false}">
						<tr>
							<td colspan=2><c:out value="${it}"/></td>
						</tr>
					</c:if>
				</c:forEach>
				<tr>
					<td colspan=2>*************************************************************************</td>
				</tr>	
			</c:forEach>
		</tbody>
	</table>
</div>

<div class="panelBar">
    
</div>
