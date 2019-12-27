<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="alert.css"/>
<script type="text/javascript" src="jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="alert.js"></script>
<title>Insert title here</title>
<style type="text/css">
.alertData input, .alertData select{
	width:200px;
	height: 25px;
}

.selItem{
	background-color: #FFCC99;
	cursor: pointer;
}

.markItem{
	margin: 5px 0px;
	padding: 2px;
	border: 1px solid #CCCCCC;
}

</style>
</head>
<body>

<%
Map<String,Object> item = (Map<String,Object>)request.getAttribute("item");
List<Map<String, Object>> markList = (List<Map<String, Object>>)request.getAttribute("markList");
File xmlFile = new File(item.get("filePath").toString());
int fileId = Integer.parseInt(request.getParameter("id"));
%>

文件名：<%=xmlFile.getName() %> <br/><br/>

<button onclick="history.back(-1);">返回</button><br/><br/>

<a href="showFile?id=<%=item.get("id")%>" target="f_<%=xmlFile.getName() %>">原始文件</a><br/><br/>

<a href="showSessions?id=<%=item.get("id")%>" target="s_<%=xmlFile.getName() %>">展示会话信息</a><br/><br/>


标注项
<div class="needReceiveContains">
<%for(Map<String, Object> markItem : markList){%>
<div class="markItem" dataid="<%=markItem.get("id")%>"><%=markItem.toString() %></div>
<%} %>
</div>

<br/>
<br/>


<script type="text/javascript">

$('.needReceiveContains').delegate('.markItem','click',function(){
	$('.markItem').removeClass('selItem');
	$(this).addClass('selItem');
});

</script>

</body>
</html>