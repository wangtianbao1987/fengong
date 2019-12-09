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

<div class="userForm">
姓名：<input type="text" id="userName" />
<br/><br/>
<button class="subUserBtn">确定</button>

</div>
<div class="datas">
<div>当前接收人： <span class="currentUser"></span></div>

<div><a href="result" target="res">结果统计</a></div>

<table style="width:100%" border="1" bordercolor="#CCC">
	<thead class="title">
		<tr>
		<th>文件名</th>
		<th>接收人</th>
		<th>状态</th>
		</tr>
	</thead>
	<tbody class="content"></tbody>
</table>
</div>

<script type="text/javascript">

var handleLocalStorage = function(method, key, value) {
  switch (method) {
    case 'get' : {
      let temp = window.localStorage.getItem(key);
      if (temp) {
        return temp
      } else {
        return false
      }
    }
    case 'set' : {
      window.localStorage.setItem(key, value);
      break
    }
    case 'remove': {
      window.localStorage.removeItem(key);
      break
    }
    default : {
      return false
    }
  }
};

var initShow = function() {
	var userName = handleLocalStorage('get', 'userName');
	if(userName) {
		$('.userForm').hide();
		$('.datas').show();
		$('.currentUser').html(userName);
		loadDatas();
	} else {
		$('.datas').hide();
		$('.userForm').show();
	}
};

$('.subUserBtn').unbind('click').bind('click',function(){
	if($.trim($('#userName').val()) == '') {
		alert('接收人不能为空');
		return;
	}
	handleLocalStorage('set', 'userName', $.trim($('#userName').val()));
	initShow();
});

var loadDatas = function(){
	$.post('queryFiles',{},function(datas){
		$('.content').html('');
		$.each(datas,function(i, data){
			var tr = $('<tr>');
			var td1 = $('<td>');
			var td2 = $('<td>');
			if(data.userName && data.userName != handleLocalStorage('get', 'userName')) {
				td1.html(data.filePath);
			} else {
				td1.html('<a href="receive?id='+data.id+'&userName='+encodeURI(encodeURI(handleLocalStorage('get', 'userName')))+'">' + data.filePath + '</a>');
			}
			td2.html(data.userName);
			var td3 = $('<td>');
			td3.html(data.receiveStatus==0?'未接收':(data.receiveStatus==1?'接收未处理':(data.receiveStatus==2?'已处理':'未接收')));
			tr.append(td1).append(td2).append(td3);
			$('.content').append(tr);
		});
	},'json');
};





initShow();

</script>

</body>
</html>