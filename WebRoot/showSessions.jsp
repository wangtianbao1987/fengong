<%@page import="java.io.File"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
.r0{
	line-height: 25px;
	text-align: left;
	border: 1px solid #99FFCC;
}
.r1{
	line-height: 25px;
	text-align: right;
	border: 1px solid #FFCC99;
}
</style>
</head>
<body>

<%
Map<String,Object> item = (Map<String,Object>)request.getAttribute("item");
File xmlFile = new File(item.get("filePath").toString());
%>

文件名：<%=xmlFile.getName() %> <br/><br/>

<a href="showFile?id=<%=item.get("id")%>" target="f_<%=xmlFile.getName() %>">原始文件</a><br/><br/>

<%
String sessions = item.get("sessions").toString();
String[] sessionArr = sessions.split("\n");
int r0Index = 0;
int r1Index = 0;
for(String sessionStr : sessionArr) {
	if (sessionStr.startsWith("R0 - ")) {
		out.println("<fieldset class='r0'><legend> R0 - 第"+r0Index+"条</legend>"+sessionStr.substring(5)+"</fieldset>");
		r0Index++;
	} else {
		out.println("<fieldset class='r1'><legend> R1 - 第"+r1Index+"条</legend>"+sessionStr.substring(5)+"</fieldset>");
		r1Index++;
	}
}

%>

</body>
</html>