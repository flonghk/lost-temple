<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<script type="text/javascript">
	
</script>
<form id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="execute/running">
	<input type="hidden" name="orderField" value="${pagerForm.orderField}" />
	<input type="hidden" name="orderDirection" value="${pagerForm.orderDirection}" />
	<input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />    
</form>

<div class="pageHeader">	
	<form style="float:left" id="runninglistLoad" rel="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="execute/running" >
		<div class="searchBar">
		</div>
	</form>
</div>

<div class="pageContent">	
	<div class="panelBar">
    </div>
	<table class="table" width="100%" layoutH="110" nowrapTD="false" >
		<thead>
			<tr>
				<th width="10%">ID</th>
				<th width="25%">名称</th>	
				<th width="30%">开始时间</th>	
				<th width="25%">状态</th>	
				<th width="10%">操作</th>	
			</tr>
		</thead>
		<tbody>
 			<c:forEach var="item" items="${list}" varStatus="index">
				<tr>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${item.key}" /></td>
					<td><date:date value="${item.key}" parttern="yyyy-MM-dd  HH:mm:ss"/></td>
					<td><c:out value="${item.value}" /></td>
					<td>
						<c:if test="${item.value=='正在运行'}">
							<a class="btnLook" href="execute/running/${item.key}" mask="true" rel="runninglogdetails${item.key}" target="navtab" title="运行LOG">运行LOG</a>  
						</c:if>   
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
