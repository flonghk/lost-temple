function selectedRowCss(contentId, tbodyId, obj){
	if(!$(obj).hasClass("selected")){
		$("#"+contentId+" #"+tbodyId+">tr").filter(".selected").removeClass("selected");
		$(obj).addClass("selected");
		//$("#"+contentId+" #suggest").hide();
	}
}

function addRow(contentId, tbodyId){
	var index = parseInt($("#"+contentId+" .size").val())+1;
	$("#"+contentId+" .size").val(index);
	$("#"+contentId+" #"+tbodyId).append("<tr id='"+index+"' onclick=\"selectedRowCss('"+contentId+"','"+tbodyId+"',this)\">"+
								"<td class=\"addUnit1\">"+index+"</td>"+
								"<td class=\"addUnit2\"><input class=\"xmlinput textInput\" type=\"text\"></td>"+
								"<td class=\"addUnit3\"><input class=\"xmlinput textInput\" type=\"text\"></td>"+
								"<td class=\"addUnit4\"><a class=\"btnDel\" href=\"#\" onclick=\"deleteRow('"+contentId+"','"+index+"')\">删除</a></td></tr>");
}

function addDataRow(name, tbIndex){
	var contentId = "testdatadetails_"+name+"_"+tbIndex;
	var tbodyId = "testdatadetailstable_"+tbIndex;
	var index = parseInt($("#"+contentId+" .size").val())+1;
	$("#"+contentId+" .size").val(index);
	$("#"+contentId+" #"+tbodyId).append("<tr id='"+index+"' onclick=\"selectedRowCss('"+contentId+"','"+tbodyId+"',this)\">"+
								"<td class=\"addUnit1\">"+index+"</td>"+
								"<td class=\"addUnit2\"><input class=\"xmlinput textInput\" type=\"text\"></td>"+
								"<td class=\"addUnit3\"><input class=\"xmlinput textInput\" type=\"text\"><a height=\"800\" width=\"800\" rel=\"jsonformatter\" target=\"dialog\" href=\"testdata/json/"+name+"/"+tbIndex+"/"+index+"\" class=\"btnSelect\">格式化JSON</a></td>"+
								"<td class=\"addUnit4\"><a class=\"btnDel\" href=\"#\" onclick=\"deleteRow('"+contentId+"','"+index+"')\">删除</a></td></tr>");
	$("#"+contentId+" #"+tbodyId).initUI();
	initLayout();
}

function addCaseRow(contentId, tbodyId, className, classIndex){
	var index = parseInt($("#"+contentId+" .size").val())+1;
	$("#"+contentId+" .size").val(index);
	$("#"+contentId+" #"+tbodyId).append("<tr id='"+index+"' onclick=\"selectedRowCss('"+contentId+"','"+tbodyId+"',this)\">"+
								"<td class=\"addUnit1\">"+index+"</td>"+
								"<td class=\"addUnit2\">" +
								"<select class=\"xmlinput\" ref=\""+className+"\" refindex=\""+classIndex+"\" isclass=\"\" onchange=\"selectObject('"+contentId+"', '"+tbodyId+"', this)\">"+
								"</select>"+
								"</td>"+
								"<td class=\"addUnit3\"><input class=\"xmlinput textInput\" type=\"text\"></td>"+
								"<td class=\"addUnit4\"><input class=\"xmlinput textInput\" type=\"text\"></td>"+
								"<td class=\"addUnit5\"><a class=\"btnDel\" href=\"#\" onclick=\"deleteCaseRow('"+contentId+"', this)\">删除</a></td></tr>");
	var addRowSelect = $("#"+contentId+" #"+tbodyId+" tr").last().children().eq(2).children().first();
	addRowSelect.on("focus",function(){
		focusRes(contentId, this);
	});
	addRowSelect.on("mouseover",function(){
		blurRes(this);
	});
	addRowSelect.on("mouseout",function(){
		unblurRes(contentId, this);
	});
}

function insertCaseRow(contentId, tbodyId, className, classIndex){
	var index = parseInt($("#"+contentId+" .size").val())+1;
	$("#"+contentId+" .size").val(index);
	$("#"+contentId+" #"+tbodyId+" tr.selected").before("<tr id='"+index+"' onclick=\"selectedRowCss('"+contentId+"','"+tbodyId+"',this)\">"+
								"<td class=\"addUnit1\">"+index+"</td>"+
								"<td class=\"addUnit2\">" +
								"<select class=\"xmlinput\" ref=\""+className+"\" refindex=\""+classIndex+"\" isclass=\"\" onchange=\"selectObject('"+contentId+"', '"+tbodyId+"', this)\">"+
								"</select>"+
								"</td>"+
								"<td class=\"addUnit3\"><input class=\"xmlinput textInput\" type=\"text\"></td>"+
								"<td class=\"addUnit4\"><input class=\"xmlinput textInput\" type=\"text\"></td>"+
								"<td class=\"addUnit5\"><a class=\"btnDel\" href=\"#\" onclick=\"deleteCaseRow('"+contentId+"', this)\">删除</a></td></tr>");
	var addRowSelect = $("#"+contentId+" #"+tbodyId+" tr.selected").prev().first().children().eq(2).children().first();
	addRowSelect.on("focus",function(){
		focusRes(contentId, this);
	});
	addRowSelect.on("mouseover",function(){
		blurRes(this);
	});
	addRowSelect.on("mouseout",function(){
		unblurRes(contentId, this);
	});
}

function deleteRow(contentId, trId){
	$("#"+contentId+" tr").remove("#"+trId);
}

function deleteCaseRow(contentId, obj){
	var trobj = $(obj).parent().first().parent().first();
	var sel = $(trobj).find("select").first();
	var isclass = $(sel).attr("isclass");
	if(isclass=="0" || isclass==""){
		$(trobj).remove();
	}
	if(isclass=="1"){
		var tbody = $(trobj).parent().first();
		var refindex = $(sel).attr("refindex");
		var ref = $(sel).attr("ref");
		$(tbody).find("select[ref='"+ref+"'][refindex='"+refindex+"']").each(function(i){
			$(this).parent().first().parent().first().remove();
		});
		var lastSels = $(tbody).find("select[isclass='1']");
		if(lastSels.length==0){
			$("#"+contentId+" .lastClassName").val($("#"+contentId+" .defaultClassName").val());
		}
		if(lastSels.length>0){
			$("#"+contentId+" .lastClassName").val($(lastSels.last()).attr("ref"));
		}
	}
	
	
}

function checkKey(contentId, tbodyId){
	var flag = true;
	$("#"+contentId+" #"+tbodyId+" tr").each(function(){
		var inputs = $(this).find(".xmlinput");
		var key = $(inputs[0]).val();
		if($.trim(key)=="" || /^\d+$/.test($.trim(key))){
			flag = false;
			return flag;
		}
	});
	return flag;
}

function generateJson(contentId, tbodyId){
	var json = new Object(); 
	var key = new Array(); 
	var index = 0;
	$("#"+contentId+" #"+tbodyId+" tr").each(function(){
		var inputs = $(this).find(".xmlinput");
		var v = "";
		if(inputs.length>1){
			v = $(inputs[1]).val();
		}else{
			v = $(this).find(".xmlarea").first().val();
			v = JSON.stringify(JSON.parse(v),null,'');
		}
		key[index] = $(inputs[0]).val();
		eval("json."+key[index]+" = \'"+v+"\'");
		index++;
	});
	return JSON.stringify(json,key); 
}

function saveXml(contentId, tbodyId, url){
	var flag = checkKey(contentId, tbodyId);
	if(flag){
		var json = generateJson(contentId, tbodyId);
		$.post(url,{"data":json}, saveXmlCallback);
	}else{
		alertMsg.error("key不能为空或不能为数字！");
	}
}

function saveXmlCallback(json){
	DWZ.ajaxDone(json);		
	var info = JSON.parse(json);		
	if(info.statusCode == "200") {			
		alertMsg.info(info.message);
	}else {			
		alertMsg.error(info.message)
	}
}

function saveCaseXmlCallback(json){
	DWZ.ajaxDone(json);		
	var info = JSON.parse(json);		
	if(info.statusCode == "200") {			
		alertMsg.info(info.message);
		navTab.reload(info.forwardUrl, {navTabId: json.navTabId});
	}else {			
		alertMsg.error(info.message)
	}
}


function addTable(name){
	var tableIndex = $("#testdatadetails_"+name+" #tableSize").val();
	var index = parseInt(tableIndex)+1;
	$("#testdatadetails_"+name+" #tableSize").val(index);
	var methodName = $("#testdatadetails_"+name+" #testmethod").val();
	var closeButtonHtml = "";
	if(methodName!='common'){
		closeButtonHtml = "<a class=\"delete\" href=\"#\" onclick=\"delTable(this)\"><span>删除</span></a>";
	}
	var tableHtml = "<div class=\"unitBox\" style=\"float:left; display:block; overflow:auto; width:30%; padding:0px 1px\">"+
					"	<div class=\"pageContent\" name=\"testdatadetails\" index=\""+index+"\" id=\"testdatadetails_"+name+"_"+index+"\" style=\"border-left:1px #B8D0D6 solid;border-right:1px #B8D0D6 solid\">"+
					"		<div class=\"panelBar\">"+
					"			<ul class=\"toolBar\">"+
					"				<li><a class=\"icon\" href=\"#\"><span>测试数据</span></a>"+
					"				</li>"+
					"				<li style=\"float:right;\">"+
										closeButtonHtml+
					"					<a class=\"add\" href=\"#\" onclick=\"addDataRow('"+name+"', '"+index+"')\"><span>新增</span></a>"+
					"				</li>"+
					"			</ul>"+
					"			<input class=\"size\" type=\"hidden\" value=\"0\">"+
					"			<input class=\"iscommon\" type=\"hidden\" value=\""+methodName+"\">"+
					"		</div>"+
					"		<table class=\"table\" width=\"100%\" layoutH=\"300\">"+
					"			<thead>"+
					"				<tr>"+
					"					<th class=\"addUnit1\">序号</th>"+
					"					<th class=\"addUnit2\">数据名</th>"+
					"					<th class=\"addUnit3\">数据值</th>	"+
					"					<th class=\"addUnit4\">操作</th>"+
					"				</tr>"+
					"			</thead>"+
					"			<tbody id=\"testdatadetailstable_"+index+"\">"+
					"			</tbody>"+
					"		</table>"+
					"		<div class=\"panelBar\">"+
					"			<div class=\"pages\">"+
					"				<span>共0条</span>"+
					"			</div>				"+
					"		</div>"+
					"	</div>"+
					"</div>";
	$("#testdatadetails_"+name).append(tableHtml);
	//$("#testdatadetails_"+name+" div.gridScroller").attr("layoutH","300");
	$("#testdatadetails_"+name).initUI();
	initLayout();
	//$('table.table').initUI();
	//$("#testdatadetails_"+name+" div.gridScroller").last().css("height",parseInt($("#testdatadetails_"+name).parents("div.layoutBox:first").height)-300);
}

function getCursorPosition(inputObj){
	var obj = $(inputObj)[0];
	var cursurPosition=-1;
	if(navigator.userAgent.indexOf("Firefox")!=-1 || navigator.userAgent.indexOf("Chrome")){//非IE浏览器
		cursurPosition= obj.selectionStart;
	}else{//IE
		var range = document.selection.createRange();
		range.moveStart("character",-obj.value.length);
		cursurPosition=range.text.length;
  }
  return cursurPosition;
}

function selectedSuggest(contentId, tbodyId, obj){
	var txt = "${"+$(obj).text()+"}";
	var selectedInput = $("#"+contentId+" #"+tbodyId+" tr.selected>td").eq(2).children().first();
	var index = getCursorPosition($(selectedInput));
	var inputVal = $(selectedInput).val();
	$(selectedInput).val(inputVal.substring(0,index)+txt+inputVal.substring(index));
	$("#"+contentId+" #suggest").hide();
	$("#"+contentId).find("input.refSuggest").removeClass("refSuggest");
}

function getAllData(contentId,tbodyId,obj){
	var selectedTr = $(obj).parent().first().parent().first();
	$("#"+contentId+" #suggest").hide();
	var input = $(obj).val();
	var index = getCursorPosition($(obj));
	var arr = new Array();
	var count = 0;
	var prevTr = $(selectedTr).prevAll();
	$(prevTr).each(function(i){
		var v = $(this).children().eq(3).children().first().val();
		if($.trim(v)!=""){
			arr[count] = $.trim(v);
			count++;
		}
	});
	var json = JSON.stringify(arr);
}

function focusRes(contentId,obj){
	var position = $(obj).position();
	$("#"+contentId+" #suggest").css("left",position.left);
	$("#"+contentId+" #suggest").css("top",position.top+$(obj).height()*4.5);//+$("#"+contentId+" #suggest").height()
	returnValue(contentId,obj);
	$("#"+contentId+" #suggest").show();
	$(obj).addClass("refSuggest");
	//getAllData(contentId,tbodyId,obj);
}

function blurRes(obj){
	//$(obj).addClass("mouseoverclass");
	//console.log(0);
	$(obj).unbind("blur");
	//setTimeout("$(\"#"+contentId+" #suggest\").hide()", 200);
}

function unblurRes(contentId, obj){
	//$(obj).addClass("mouseoverclass");
	$(obj).bind("blur",function(){
		$("#"+contentId+" #suggest").hide();
		$("#"+contentId).find("input.refSuggest").removeClass("refSuggest");
		//console.log(1);
		//setTimeout("$(\"#"+contentId+" #suggest\").hide()", 200);
	});
}

function removeBlur(contentId){
	var inputsel = $("#"+contentId).find("input.refSuggest");
	$(inputsel).unbind("blur");
	//console.log($(inputsel).val());
}

function addBlur(contentId){
	var inputsel = $("#"+contentId).find("input.refSuggest");
	$(inputsel).bind("blur",function(){
		$("#"+contentId+" #suggest").hide();
		$("#"+contentId).find("input.refSuggest").removeClass("refSuggest");
		//setTimeout("$(\"#"+contentId+" #suggest\").hide()", 200);
	});
	//$("#"+contentId).find("input.refSuggest").removeClass("refSuggest");
}

function returnValue(contentId, obj){
	$("#"+contentId+" #suggest ul li").filter(".add").remove();
	var selectedTr = $(obj).parent().first().parent().first();
	var prevTr = $(selectedTr).prevAll();
	$(prevTr).each(function(i){
		var tbodyId = $(this).parent().first().attr("id");
		var v = $(this).children().eq(3).children().first().val();
		if($.trim(v)!=""){
			$("#"+contentId+" #suggest ul").first().append("<li class=\"add\">"+$.trim(v)+"</li>");
			var addLi = $("#"+contentId+" #suggest ul li").filter(".add").last();
			$(addLi).on("click", function(){
				selectedSuggest(contentId,tbodyId,this);
			});
		}
	});
}

LoadLogObject = function(name){
	
	eval("var log"+name+" = window.setInterval(loadLog,1000);");
	
	function loadLogCallback(){
		return function(json){
			DWZ.ajaxDone(json);		
			var info = JSON.parse(json);
			if(info.statusCode == "200") {
				//$("#runninglog_"+name+" #log_"+name).html("<div>"+info.data+"</div>");
				appendLog(info.data);
			}else if(info.statusCode == "201"){
				appendLog(info.data);
				clearInt();
			}else if(info.statusCode == "202"){
				clearInt();
			}else{
				clearInt();
				appendLog(info.data);
			}
		}
	}
	
	function appendLog(list){
		$("#runninglog_"+name+" #index_"+name).val(parseInt($("#runninglog_"+name+" #index_"+name).val())+list.length);
		$.each(list, function(i, n){
			$("#runninglog_"+name+" #log_"+name).append("<br><p>");
			$("#runninglog_"+name+" #log_"+name).append(encodeHtml(n));
			$("#runninglog_"+name+" #log_"+name).append("</p>");
		});
	}

	function loadLog(){
		$.ajaxSettings.global=false;
		$.post("execute/look",{"name":$("#runninglog_"+name+" #h_"+name).val(),"index":$("#runninglog_"+name+" #index_"+name).val()}, loadLogCallback(name));
		$.ajaxSettings.global=true;
	}	

	function setInt(name){
		eval("log"+name+" = window.setInterval(\"loadLog\",2000);");
	}

	function clearInt(){
		eval("window.clearInterval(log"+name+")");
	}
}

var loadId;

LoadRunningLog = function(){
	
	if(loadId){
		window.clearInterval(loadId);
	}
	
	loadId = window.setInterval(loadRunningLog,5000);

	function loadRunningLog(){
		if(navTab.isExistTab("runninglist") && navTab.isCurrentTab("runninglist")){
			$.ajaxSettings.global=false;
			//navTab.reload("execute/running");
			//$.post("execute/running");
			//$("#runninglistLoad").submit();
			navTab.reload("execute/running", {navTabId: "runninglist"});
			$.ajaxSettings.global=true;
		}
	}	
}

function encodeHtml(s){
	var REGX_HTML_ENCODE = /"|&|'|<|>|[\x00-\x20]|[\x7F-\xFF]|[\u0100-\u2700]/g;
	return (typeof s != "string") ? s : s.replace(REGX_HTML_ENCODE, function($0){
        var c = $0.charCodeAt(0), r = ["&#"];
        c = (c == 0x20) ? 0xA0 : c;
        r.push(c); r.push(";");
        return r.join("");
      });
}

function getJsonString(v, flag){
	try{
		return JSON.stringify(JSON.parse(v),null,flag);
	}catch(err){
		return v;
	}
}
