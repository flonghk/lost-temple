<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>
<script>	
	
	function validInput(){
		var s = $(".testcaseadd #name").val();
		var re = /^[A-Z][a-zA-Z0-9]*$/;
		if(!re.test(s)){
			alertMsg.error("请输入正确的测试数据名!");
			return false;
		}
		return true;
	}
	
	$(".testcaseadd #submit").on("click",function(){		
		if(!validInput()){
			return;
		}
		$(".testcaseadd #postForm").submit();
	});

</script>

<div class="pageContent testcaseadd">
	<form method="post" id="postForm" action="testcase/add" class="pageForm required-validate"
			onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="50">
			<div class="unit">
				<label>测试用例名:</label>
				<input type="text" id="name" name="name" maxlength="64"><span style="display:block;padding:5px 0px;">*只允许英文，首字母大写.</span>
			</div>
		</div>		
	</form>
	<div class="panelBar">
		<button type="submit" id="submit">确定</button>
	</div>
</div>