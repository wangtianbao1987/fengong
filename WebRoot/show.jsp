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

<a href="showFile?id=<%=item.get("id")%>" target="f_<%=xmlFile.getName() %>">原始文件</a><br/><br/>

<a href="showSessions?id=<%=item.get("id")%>" target="s_<%=xmlFile.getName() %>">展示会话信息</a><br/><br/>

<button class="addBtn">添加应召回项</button><br/><br/>

标注项:(双击删除)
<div class="needReceiveContains">
<%for(Map<String, Object> markItem : markList){%>
<div class="markItem" title="双击删除" dataid="<%=markItem.get("id")%>"><%=markItem.toString() %></div>
<%} %>
</div>

<br/>
<br/>

<button class="finishBtn">应召回项全部标记完成</button>


<script type="text/javascript">

$('.addBtn').unbind('click').bind('click',function(){
	myAlert({
		message:'<div class="alertData">类型：<select class="type"><option value="tel">手机号码</option><option value="idCard">身份证号</option><option value="jobNum">工号</option><option value="sex">性别</option><option value="age">年龄</option><option value="address">地址</option><option value="yinlian">银行卡号</option><option value="personName">客户姓名</option><option value="chepai">车牌号</option><option value="chejia">车架号</option><option value="baodan">保单号</option></select><br/>'
			+ '角色：<select class="roleName"><option value="R0">R0</option><option value="R1">R1</option></select><br/>'
			+ '开始位置：<input type="number" class="startItem" placeholder="第几条记录"/> <input type="number" class="startWordsIndex" placeholder="索引位置"/><br/>'
			+ '结束位置：<input type="number" class="endItem" placeholder="第几条记录"/> <input type="number" class="endWordsIndex" placeholder="索引位置"/><br/>'
			+ '应召回内容：<input type="text" class="recevCont" style="width:100%"></div>',
		btns:[{
			txt:'确定',
			click:function(div,dd){
				try{
					var type = div.find('.type').val();
					var roleName = div.find('.roleName').val();
					var startItem = parseInt(div.find('.startItem').val());
					var startWordsIndex = parseInt(div.find('.startWordsIndex').val());
					var endItem = parseInt(div.find('.endItem').val());
					var endWordsIndex = parseInt(div.find('.endWordsIndex').val());
					var recevCont = div.find('.recevCont').val();
					var reqParam = {
							type : type ,
							roleName : roleName ,
							startItem : startItem ,
							startWordsIndex : startWordsIndex ,
							endItem : endItem ,
							endWordsIndex : endWordsIndex ,
							recevCont : recevCont,
							filePath: '<%=xmlFile.getAbsolutePath().replace("\\", "\\\\") %>'
						};
					$.post('mark',reqParam,function(data){
						if(data.substring(0,3) == 'id='){
							dataid = data.substring(3);
							var div = $('<div>');
							div.addClass('markItem');
							div.attr('title', '双击删除');
							div.attr('dataid', dataid);
							div.html(JSON.stringify(reqParam));
							$('.needReceiveContains').append(div);
							closeMyAlert();
						} else{
							alert(data);
						}
					});
				} catch(e){
					alert('error');
				}
			}
		},{
			txt:'取消',
			click:function(div, dd) {
				closeMyAlert();
			}
		}]
	});
});

$('.needReceiveContains').delegate('.markItem','dblclick',function(){
	var me = this;
	$(me).addClass('selItem');
	var dataId = $(me).attr('dataid');
	if(confirm('确定要删除该项吗？')){
		$.post('delMark',{dataId:dataId},function(data){
			if(data == '0') {
				$(me).remove();
			}
		});
	}
	$(me).removeClass('selItem');
});

$('.needReceiveContains').delegate('.markItem','click',function(){
	$('.markItem').removeClass('selItem');
	$(this).addClass('selItem');
});

$('.finishBtn').unbind('click').bind('click',function(){
	location.href="finishMark?fileId=<%=fileId%>";
});

</script>

</body>
</html>