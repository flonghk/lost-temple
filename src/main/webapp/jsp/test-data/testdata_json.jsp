<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>
<script>	
	
	function validInput(){
		var s = $(".testdataadd #name").val();
		var re = /^[A-Z][a-zA-Z0-9]*$/;
		if(!re.test(s)){
			alertMsg.error("请输入正确的测试数据名!");
			return false;
		}
		return true;
	}
	
	$(".testdataadd #submit").on("click",function(){		
		if(!validInput()){
			return;
		}
		$(".testdataadd #postForm").submit();
	});
	
	function jsonSave(){
		var v = $("#testdatajson_${name} #jsoninput").val();
		v = getJsonString(v,'');
		$("#testdatadetails_${name} #testdatadetailstable_${table} tr#${tr} td.addUnit3 input.xmlinput").val(v);
	}
	
	/* function getJsonString(v, flag){
		try{
			return JSON.stringify(JSON.parse(v),null,flag);
		}catch(err){
			return v;
		}
	} */
	
	function jsonFormatter(){
		var v = $("#testdatajson_${name} #jsoninput").val();
		v = getJsonString(v,'  ');
		$("#testdatajson_${name} #jsoninput").val(v);
	}
	
	$(function(){
		var inputV = $("#testdatadetails_${name} #testdatadetailstable_${table} tr#${tr} td.addUnit3 input.xmlinput").val();
		$("#testdatajson_${name} #jsoninput").val(getJsonString(inputV,'  '));
	});
</script>


<div class="pageContent" id="testdatajson_${name}" style="width:100%; height: 100%;">
	
	<div class="dialogContent layoutBox unitBox" style="height: 95%;">
		<textarea style="width:100%;height:100%" class="textInput" id="jsoninput"></textarea>
	</div>
	<div class="formBar">
		<ul>
			<li>
				<div class="buttonActive"><div class="buttonContent"><button type="button" onclick="jsonFormatter()">格式化JSON</button></div></div>
			</li>
			<li>
				<div class="buttonActive"><div class="buttonContent"><button class="close" type="button" onclick="jsonSave()">保存</button></div></div>
			</li>
			<li>
				<div class="button"><div class="buttonContent"><button class="close" type="button">取消</button></div></div>
			</li>
		</ul>
	</div>

</div>
