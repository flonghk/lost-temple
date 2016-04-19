<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<script type="text/javascript">

	function checkedAll(){
		if($("#testcaselistpage #checkboxall").attr("checked")){
			$("#testcaselistpage input[name='checktestcase']").attr("checked", true);
			$("#testcaselistpage input[name='checktestcase']:checked").each(function(i){
				var v = $("#testcaselistpage #checkInfo").val();
				$("#testcaselistpage #checkInfo").val(addInfo(v, $(this).val()));
			});
		}else{
			$("#testcaselistpage input[name='checktestcase']").attr("checked", false);
			$("#testcaselistpage #checkInfo").val("");
		}
	}
	
	function checkedItem(obj){
		if($(obj).attr("checked")){
			var v = $("#testcaselistpage #checkInfo").val();
			$("#testcaselistpage #checkInfo").val(addInfo(v, $(obj).val()));
		}else{
			var v = $("#testcaselistpage #checkInfo").val();
			$("#testcaselistpage #checkInfo").val(delInfo(v, $(obj).val()));
		}
	}
	
	function addInfo(source, sub){
		if(source==""){
			return sub;
		}
		var ss = source.split(",");
		var r = true;
		$.each(ss, function(i,n){
			if(n==sub){
				r = false;
				return false;
			}
		});
		if(r){
			return source+","+sub;
		}else{
			return source;
		}
	}
	
	function delInfo(source, sub){
		if(source==""){
			return source;
		}
		var ss = source.split(",");
		var r = "";
		$.each(ss, function(i,n){
			if(n!=sub){
				if(r == ""){
					r = n;
				}else{
					r = r + "," + n;
				}
			}
		});
		return r;
	}
	
	function executeTestCallback(json){
		DWZ.ajaxDone(json);		
		var info = JSON.parse(json);
		var str = "";
		if(info.statusCode == "200") {	
			alertMsg.info(info.message);
		}else{
			alertMsg.error(info.message);
		}
	}
	
	function executeTest(){
		var v = $("#testcaselistpage #checkInfo").val();
		if(v==""){
			alertMsg.error("请至少选择一个用例！");
		}else{
			$.post("execute/senario",{"names":v},executeTestCallback);
		}
	}

	$(function(){
		$.each($("#testcaselistpage #checkInfo").val().split(","), function(i, n){
			$("#testcaselistpage input[name='checktestcase'][value='"+n+"']").attr("checked", true);
		});
	});
</script>
<form id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="testcase/list">
	<input type="hidden" name="name" value="${pagerForm.name}" />
	<input type="hidden" name="orderField" value="${pagerForm.orderField}" />
	<input type="hidden" name="orderDirection" value="${pagerForm.orderDirection}" />
	<input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />    
</form>

<div class="pageHeader">	
	<form style="float:left" rel="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="testcase/list" >
		<div class="searchBar">
			<label>用例名称：</label>
			<input type="text" name="name" value="<c:out value="${pagerForm.name}"/>"/>
			<button type="submit" name="searchBtn" id="searchId">查询</button>
		</div>
	</form>
</div>

<div class="pageContent" id="testcaselistpage">	
	<div class="panelBar">
        <ul class="toolBar">
            <li><a class="add" href="testcase/add" target="dialog" mask="true" mask="true" rel="testcaseadd" title="新增测试用例"><span>新增测试用例</span></a></li>
            <li><a class="add" href="#" onclick="executeTest()"><span>运行</span></a></li>
        </ul>
        <input type="hidden" id="checkInfo">
    </div>
	<table class="table" width="100%" layoutH="110" nowrapTD="false" >
		<thead>
			<tr>
				<th width="10%"><input type="checkbox" name="checkboxall" id="checkboxall" onclick="checkedAll()">全选</th>
				<th width="10%">ID</th>
				<th width="60%">名称</th>	
				<th width="10%">操作</th>	
			</tr>
		</thead>
		<tbody>
 			<c:forEach var="item" items="${list}" varStatus="index">
				<tr>
					<td><input type="checkbox" name="checktestcase" value="${item}" onclick="checkedItem(this)"></td>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${item}" /></td>
					<td>
                    	<a class="btnEdit" href="testcase/update/${item}" target="dialog" mask="true" rel="testcaseupdate" title="编辑${item}">编辑</a>
						<a class="btnLook" href="testcase/details/${item}" mask="true" rel="testcasedetails" target="navtab" title="${item}">详情</a>     
                	</td>
				</tr> 			
			</c:forEach>
		</tbody>
	</table>
</div>

<div class="panelBar">
    <div class="pages">
        <span>显示</span> 
            <select class="select" name="numPerPage"
                onchange="navTabPageBreak({numPerPage:this.value})">
                <option value="20" <c:if test="${pagerForm.numPerPage == 20}"> selected </c:if> >20</option>
                <option value="50" <c:if test="${pagerForm.numPerPage == 50}"> selected </c:if> >50</option>
                <option value="100" <c:if test="${pagerForm.numPerPage == 100}"> selected </c:if> >100</option>
                <option value="500" <c:if test="${pagerForm.numPerPage == 500}"> selected </c:if> >500</option>
            </select> 
        <span>条，共 ${pagerForm.totalCount} 条，共 ${pagerForm.totalPageCount} 页</span>
    </div>

    <div class="pagination" targetType="navTab"
        totalCount="${pagerForm.totalCount}"
        numPerPage="${pagerForm.numPerPage}"
        pageNumShown="${pagerForm.pageNumShown}"
        currentPage="${pagerForm.pageNum}">
    </div>
</div>
