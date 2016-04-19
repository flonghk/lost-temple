<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<script type="text/javascript">
	
</script>
<form id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="execute/ran">
	<input type="hidden" name="orderField" value="${pagerForm.orderField}" />
	<input type="hidden" name="orderDirection" value="${pagerForm.orderDirection}" />
	<input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />    
</form>

<div class="pageHeader">	
	<form style="float:left" rel="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="execute/ran" >
		<div class="searchBar">
			<label>报告名称：</label>
			<input type="text" name="name" value="<c:out value="${pagerForm.name}"/>"/>
			<button type="submit" name="searchBtn" id="searchId">查询</button>
		</div>
	</form>
</div>

<div class="pageContent">	
	<table class="table" width="100%" layoutH="110" nowrapTD="false" >
		<thead>
			<tr>
				<th width="10%">ID</th>
				<th width="20%">名称</th>	
				<th width="20%">时间</th>	
				<th width="10%">PASS</th>
				<th width="10%">FAIL</th>
				<th width="10%">SKIP</th>
				<th width="10%">操作</th>	
			</tr>
		</thead>
		<tbody>
 			<c:forEach var="item" items="${list}" varStatus="index">
				<tr>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${item.name}" /></td>
					<td><date:date value="${item.name}" parttern="yyyy-MM-dd  HH:mm:ss"/></td>
					<td><c:out value="${item.pass}" /></td>
					<td><c:out value="${item.fail}" /></td>
					<td><c:out value="${item.skip}" /></td>
					<td>
						<a class="btnLook" href="execute/ran/${item.all}" mask="true" rel="testranlogdetails${item.name}" target="navtab" title="测试报告详情">详情</a>     
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
