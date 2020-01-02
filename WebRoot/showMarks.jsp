<%@page import="java.io.File"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
List<Map<String, Object>> markList = (List<Map<String, Object>>)request.getAttribute("markList");
%>
<table width="100%" border="1" bordercolor="#000">
<%for(int i=0;i<markList.size();i++){
	Map<String, Object> mark = markList.get(i);
%>
<tr dataid="<%=new File(mark.get("filePath").toString()).getName() %>">
<td><%=mark.get("type") %></td>
<td><%=mark.get("recevCont") %></td>
<td><%=mark.get("markContent") %></td>
</tr>
<%} %>
</table>
</body>
</html>