<%@page import="net.sf.json.JSONArray"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="jquery-1.11.3.min.js"></script>
<style type="text/css">
.r0{
	padding : 5px;
	text-align: left;
	border: 1px solid #99FFCC;
}
.r1{
	padding : 5px;
	text-align: right;
	border: 1px solid #FFCC99;
}
.ys{
	border-bottom: 1px solid #FFFF00;
	font-weight: bolder;
	font-size: 18px;
	user-modify:read-only;
	ime-mode:disabled;
	outline:none;
}

.mark{
	border-bottom: 1px solid #FF0000;
}
.hit{
	border-bottom: 1px solid #0000FF;
}

.tel{
	color:#ff0000;
}
.idCard{
color:#00ff00;
}
.jobNum{
color:#0000ff;
}
.sex{
color:#ffff00;
}
.age{
color:#ff00ff;
}
.address{
color:#00ffff;
}
.yinlian{
color:#ff99cc;
}
.personName{
color:#ffcc99;
}
.chepai{
color:#ccff99;
}
.chejia{
color:#cc99ff;
}
.baodan{
color:#99ffcc;
}
.foot{
	height: 30px;
	width: 100%;
	background-color: #ddd;
	position: fixed;
	bottom: 0;
	text-align: center;
}

</style>
</head>
<body>

<%
Map<String,Object> item = (Map<String,Object>)request.getAttribute("item");
File xmlFile = new File(item.get("filePath").toString());

List<Map<String, Object>> markList = (List<Map<String, Object>>)request.getAttribute("markList");

List<Map<String, Object>> hitList = (List<Map<String, Object>>)request.getAttribute("hitList");

%>

文件名：<%=xmlFile.getName() %> <br/><br/>

<a href="showFile?id=<%=item.get("id")%>" target="f_<%=xmlFile.getName() %>">原始文件</a><br/><br/>

<%
String sessions = item.get("sessions").toString();
String[] sessionArr = sessions.split("\n");
int r0Index = 0;
int r1Index = 0;
for(String sessionStr : sessionArr) {
	String speakStr = sessionStr.substring(5);
	if (sessionStr.startsWith("R0 - ")) {
		out.println("<fieldset class='r0'><legend> R0 - 第"+r0Index+"条</legend><div class='ys' contenteditable >"+speakStr+"</div><div class='mark'></div><div class='hit'></div></fieldset>");
		r0Index++;
	} else {
		out.println("<fieldset class='r1'><legend> R1 - 第"+r1Index+"条</legend><div class='ys' contenteditable>"+speakStr+"</div><div class='mark'></div><div class='hit'></div></fieldset>");
		r1Index++;
	}
}

%>

<br/><br/>

<div class="foot">test</div>

<script type="text/javascript">

var printMark = function(mark,divCls, fg) {
	var fldset = $('.'+mark.roleName.toLowerCase()+':eq('+mark.startItem+')');
	var div = fldset.children('.ys');
	var toDiv = fldset.children(divCls);
	var speakStr = div.html();
	if(mark.startItem == mark.endItem){
		if(mark.startWordsIndex < 0) {
			toDiv.append('<div class="error">Error'+fg+'[startWordsIndex < 0] ' + mark.location + '</div>');
			return;
		}
		if(mark.startWordsIndex >= mark.endWordsIndex) {
			toDiv.append('<div class="error">Error'+fg+'[startWordsIndex >= endWordsIndex] ' + mark.location + '</div>');
			return;
		}
		if(speakStr.length < mark.endWordsIndex){
			toDiv.append('<div class="error">Error'+fg+'[size < endWordsIndex] ' + mark.location + '</div>');
			return;
		}
		toDiv.append('<div class="'+mark.type+'">'+mark.type + fg + speakStr.substring(mark.startWordsIndex, mark.endWordsIndex) + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;['+mark.location+']</div>');
	}else if(mark.startItem < mark.endItem){
		if(mark.startWordsIndex < 0) {
			toDiv.append('<div class="error">Error'+fg+'[startWordsIndex < 0] ' + mark.location + '</div>');
			return;
		}
		if(mark.endWordsIndex < 0) {
			toDiv.append('<div class="error">Error'+fg+'[endWordsIndex < 0] ' + mark.location + '</div>');
			return;
		}
		if(speakStr.length < mark.startWordsIndex){
			toDiv.append('<div class="error">Error'+fg+'[size < startWordsIndex] ' + mark.location + '</div>');
			return;
		}
		var res = speakStr.substring(mark.startWordsIndex);
		for(var j = 1; j< mark.endItem-mark.startItem; j++) {
			div = $('.'+mark.roleName.toLowerCase()+':eq('+(mark.startItem+j)+')').children('.ys');
			speakStr = div.html();
			res += speakStr;
		}
		div = $('.'+mark.roleName.toLowerCase()+':eq('+mark.endItem+')').children('.ys');
		speakStr = div.html();
		
		if(speakStr.length < mark.endWordsIndex){
			toDiv.append('<div class="error">Error'+fg+'[size < endWordsIndex] ' + mark.location + '</div>');
			return;
		}
		res += speakStr.substring(0, mark.endWordsIndex);
		toDiv.append('<div class="'+mark.type+'">'+mark.type + fg + res + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;['+mark.location+']</div>');
	}else {
		toDiv.append('<div class="error">Error'+fg+'[startItem > endItem] ' + mark.location + '</div>');
		return;
	}
}

var markList = <%=JSONArray.fromObject(markList).toString()%>;
var hitList = <%=JSONArray.fromObject(hitList).toString()%>;

$.each(markList,function(i,mark){
	printMark(mark, '.mark', ' === ');
});

$.each(hitList,function(i,hit){
	printMark(hit, '.hit', ' --- ');
});

var pkeyCode = 0;
var keyStatus = 0;
var keyCtrl = function(event){
	var keyCode = event.keyCode || event.which;
	if(keyStatus == 1 && pkeyCode==17){
		if(keyCode == 67) {
			pkeyCode = keyCode;
			return true;
		}
	}
	pkeyCode = keyCode;
	if(keyCode != 37 && keyCode != 39 && keyCode!=17) {
		event.returnValue = false;
		event.preventDefault();
		return false;
	}
	return true;
};

var doGetCaretPosition = function(oField) {
  var iCaretPos = 0;
  if (document.selection) {
    oField.focus();
    var oSel = document.selection.createRange();
    oSel.moveStart('character', -oField.html().length);
    iCaretPos = oSel.text.length;
  }
  else if (oField.selectionStart || oField.selectionStart == '0')
    iCaretPos = oField.selectionStart;
  return iCaretPos;
}

$('.ys').unbind('keyup').bind('keyup',function(event){
	keyStatus = 2;
	return keyCtrl(event);
});
$('.ys').unbind('keydown').bind('keydown',function(event){
	keyStatus = 1;
	return keyCtrl(event);
});

$('.ys').unbind('mouseup').bind('mouseup',function(){
    $('.foot').html('当前光标位置：' + doGetCaretPosition(this));
});

$('.ys').unbind('blur').bind('blur',function(){
	$('.foot').html('');
});

$('.ys').unbind('paste').bind('paste',function(){return false;});
$('.ys').unbind('dragenter').bind('dragenter',function(){return false;});
$('.ys').unbind('contextmenu').bind('contextmenu',function(){return false;});

</script>


</body>
</html>