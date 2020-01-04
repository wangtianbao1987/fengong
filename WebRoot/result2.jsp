<%@page import="com.pchira.fengong.utils.NumUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.pchira.fengong.utils.DBUtils"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="jquery-1.11.3.min.js"></script>
<title>Insert title here</title>
</head>
<body>
<%
Map<String, Object> res = (Map<String, Object>) request.getAttribute("res");
Map<String, Object> tel = (Map<String, Object>)res.get("tel");
Map<String, Object> idCard = (Map<String, Object>)res.get("idCard");
Map<String, Object> jobNum = (Map<String, Object>)res.get("jobNum");
Map<String, Object> sex = (Map<String, Object>)res.get("sex");
Map<String, Object> age = (Map<String, Object>)res.get("age");
Map<String, Object> address = (Map<String, Object>)res.get("address");
Map<String, Object> yinlian = (Map<String, Object>)res.get("yinlian");

tel = (tel==null?new HashMap<String,Object>() : tel);
idCard = (idCard==null?new HashMap<String,Object>() : idCard);
jobNum = (jobNum==null?new HashMap<String,Object>() : jobNum);
sex = (sex==null?new HashMap<String,Object>() : sex);
age = (age==null?new HashMap<String,Object>() : age);
address = (address==null?new HashMap<String,Object>() : address);
yinlian = (yinlian==null?new HashMap<String,Object>() : yinlian);

%>

<table style="width:100%" border="1" bordercolor="#CCC">
<tr>
<th>类型</th>
<th>召回数量</th>
<th>错误召回数量</th>
<th>未召回数量</th>
<th>解析正确数量</th>
<th>正确率</th>
<th>召回率</th>
</tr>

<tr>
<th>手机号码</th>
<td><%=NumUtils.obj2Int(tel.get("hitSum")) %></td>
<td><%=NumUtils.jian(tel.get("ct"), tel.get("hitSum")) %></td>
<td><%=NumUtils.obj2Int(res.get("telUnHitSum")) %></td>
<td><%=NumUtils.obj2Int(tel.get("succSum")) %></td>
<td><%=NumUtils.chu(tel.get("succSum"), NumUtils.jia(tel.get("ct"), res.get("telUnHitSum"))) %></td>
<td><%=NumUtils.chu(tel.get("hitSum"), NumUtils.jia(tel.get("ct"), res.get("telUnHitSum"))) %></td>
</tr>

<tr>
<th>身份证</th>
<td><%=NumUtils.obj2Int(idCard.get("hitSum")) %></td>
<td><%=NumUtils.jian(idCard.get("ct"), idCard.get("hitSum")) %></td>
<td><%=NumUtils.obj2Int(res.get("idCardUnHitSum")) %></td>
<td><%=NumUtils.obj2Int(idCard.get("succSum")) %></td>
<td><%=NumUtils.chu(idCard.get("succSum"), NumUtils.jia(idCard.get("ct"), res.get("idCardUnHitSum"))) %></td>
<td><%=NumUtils.chu(idCard.get("hitSum"), NumUtils.jia(idCard.get("ct"), res.get("idCardUnHitSum"))) %></td>
</tr>

<tr>
<th>工号</th>
<td><%=NumUtils.obj2Int(jobNum.get("hitSum")) %></td>
<td><%=NumUtils.jian(jobNum.get("ct"), jobNum.get("hitSum")) %></td>
<td><%=NumUtils.obj2Int(res.get("jobNumUnHitSum")) %></td>
<td><%=NumUtils.obj2Int(jobNum.get("succSum")) %></td>
<td><%=NumUtils.chu(jobNum.get("succSum"), NumUtils.jia(jobNum.get("ct"), res.get("jobNumUnHitSum"))) %></td>
<td><%=NumUtils.chu(jobNum.get("hitSum"), NumUtils.jia(jobNum.get("ct"), res.get("jobNumUnHitSum"))) %></td>
</tr>

<tr>
<th>性别</th>
<td><%=NumUtils.obj2Int(sex.get("hitSum")) %></td>
<td><%=NumUtils.jian(sex.get("ct"), sex.get("hitSum")) %></td>
<td><%=NumUtils.obj2Int(res.get("sexUnHitSum")) %></td>
<td><%=NumUtils.obj2Int(sex.get("succSum")) %></td>
<td><%=NumUtils.chu(sex.get("succSum"), NumUtils.jia(sex.get("ct"), res.get("sexUnHitSum"))) %></td>
<td><%=NumUtils.chu(sex.get("hitSum"), NumUtils.jia(sex.get("ct"), res.get("sexUnHitSum"))) %></td>
</tr>

<tr>
<th>年龄</th>
<td><%=NumUtils.obj2Int(age.get("hitSum")) %></td>
<td><%=NumUtils.jian(age.get("ct"), age.get("hitSum")) %></td>
<td><%=NumUtils.obj2Int(res.get("ageUnHitSum")) %></td>
<td><%=NumUtils.obj2Int(age.get("succSum")) %></td>
<td><%=NumUtils.chu(age.get("succSum"), NumUtils.jia(age.get("ct"), res.get("ageUnHitSum"))) %></td>
<td><%=NumUtils.chu(age.get("hitSum"), NumUtils.jia(age.get("ct"), res.get("ageUnHitSum"))) %></td>
</tr>

<tr>
<th>地址</th>
<td><%=NumUtils.obj2Int(address.get("hitSum")) %></td>
<td><%=NumUtils.jian(address.get("ct"), address.get("hitSum")) %></td>
<td><%=NumUtils.obj2Int(res.get("addressUnHitSum")) %></td>
<td><%=NumUtils.obj2Int(address.get("succSum")) %></td>
<td><%=NumUtils.chu(address.get("succSum"), NumUtils.jia(address.get("ct"), res.get("addressUnHitSum"))) %></td>
<td><%=NumUtils.chu(address.get("hitSum"), NumUtils.jia(address.get("ct"), res.get("addressUnHitSum"))) %></td>
</tr>

<tr>
<th>银行卡</th>
<td><%=NumUtils.obj2Int(yinlian.get("hitSum")) %></td>
<td><%=NumUtils.jian(yinlian.get("ct"), yinlian.get("hitSum")) %></td>
<td><%=NumUtils.obj2Int(res.get("yinlianUnHitSum")) %></td>
<td><%=NumUtils.obj2Int(yinlian.get("succSum")) %></td>
<td><%=NumUtils.chu(yinlian.get("succSum"), NumUtils.jia(yinlian.get("ct"), res.get("yinlianUnHitSum"))) %></td>
<td><%=NumUtils.chu(yinlian.get("hitSum"), NumUtils.jia(yinlian.get("ct"), res.get("yinlianUnHitSum"))) %></td>
</tr>

<tr>
<th>合计</th>
<td><%=NumUtils.jia(tel.get("hitSum"),idCard.get("hitSum"),jobNum.get("hitSum"),sex.get("hitSum"),age.get("hitSum"),address.get("hitSum"),yinlian.get("hitSum")) %></td>
<td><%=NumUtils.jian(NumUtils.jia(tel.get("ct"),idCard.get("ct"),jobNum.get("ct"),sex.get("ct"),age.get("ct"),address.get("ct"),yinlian.get("ct")), NumUtils.jia(tel.get("hitSum"),idCard.get("hitSum"),jobNum.get("hitSum"),sex.get("hitSum"),age.get("hitSum"),address.get("hitSum"),yinlian.get("hitSum"))) %></td>
<td>
<%=NumUtils.jia(res.get("telUnHitSum"),res.get("idCardUnHitSum"),res.get("jobNumUnHitSum"),res.get("sexUnHitSum"),res.get("ageUnHitSum"),res.get("addressUnHitSum"),res.get("yinlianUnHitSum")) %>
</td>
<td>
<%=NumUtils.jia(tel.get("succSum"),idCard.get("succSum"),jobNum.get("succSum"),sex.get("succSum"),age.get("succSum"),address.get("succSum"),yinlian.get("succSum")) %>
</td>
<td>
<%=NumUtils.chu(NumUtils.jia(tel.get("succSum"),idCard.get("succSum"),jobNum.get("succSum"),sex.get("succSum"),age.get("succSum"),address.get("succSum"),yinlian.get("succSum")), NumUtils.jia(NumUtils.jia(tel.get("ct"),idCard.get("ct"),jobNum.get("ct"),sex.get("ct"),age.get("ct"),address.get("ct"),yinlian.get("ct"), res.get("telUnHitSum"),res.get("idCardUnHitSum"),res.get("jobNumUnHitSum"),res.get("sexUnHitSum"),res.get("ageUnHitSum"),res.get("addressUnHitSum"),res.get("yinlianUnHitSum")))) %>
</td>
<td>
<%=NumUtils.chu(NumUtils.jia(tel.get("hitSum"),idCard.get("hitSum"),jobNum.get("hitSum"),sex.get("hitSum"),age.get("hitSum"),address.get("hitSum"),yinlian.get("hitSum")), NumUtils.jia(tel.get("ct"),idCard.get("ct"),jobNum.get("ct"),sex.get("ct"),age.get("ct"),address.get("ct"),yinlian.get("ct"),res.get("telUnHitSum"),res.get("idCardUnHitSum"),res.get("jobNumUnHitSum"),res.get("sexUnHitSum"),res.get("ageUnHitSum"),res.get("addressUnHitSum"),res.get("yinlianUnHitSum"))) %>
</td>
</tr>

</table>

</body>
</html>