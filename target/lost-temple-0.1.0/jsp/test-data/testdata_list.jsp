<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<script type="text/javascript">
	
</script>
<form id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="testdata/list">
	<input type="hidden" name="name" value="${pagerForm.name}" />
	<input type="hidden" name="orderField" value="${pagerForm.orderField}" />
	<input type="hidden" name="orderDirection" value="${pagerForm.orderDirection}" />
	<input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />    
</form>

<div class="pageHeader">	
	<form style="float:left" rel="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="testdata/list" >
		<div class="searchBar">
			<label>用例名称：</label>
			<input type="text" name="name" value="<c:out value="${pagerForm.name}"/>"/>
			<button type="submit" name="searchBtn" id="searchId">查询</button>
		</div>
	</form>
</div>

<div class="pageContent">	
	<div class="panelBar">
        <ul class="toolBar">
            <li><a class="add" href="testdata/add" target="dialog" mask="true" mask="true" rel="testdataadd" title="新增测试数据"><span>新增测试数据文件</span></a></li>
        </ul>
    </div>
	<table class="table" width="100%" layoutH="110" nowrapTD="false" >
		<thead>
			<tr>
				<th width="10%">ID</th>
				<th width="60%">名称</th>	
				<th width="10%">操作</th>	
			</tr>
		</thead>
		<tbody>
 			<c:forEach var="item" items="${list}" varStatus="index">
				<tr>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${item}" /></td>
					<td>
                    	<a class="btnEdit" href="testdata/update/${item}" target="dialog" mask="true" rel="testdataupdate" title="编辑${item}">编辑</a>
						<a class="btnLook" href="testdata/details/${item}" mask="true" rel="testdatadetails" target="navtab" title="测试数据详情">详情</a>     
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
