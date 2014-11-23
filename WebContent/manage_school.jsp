<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>学校管理</title>
<style type="text/css">
div#container {width:100%}
div#header    {background-color:#99bbbb;}
div#menu      {background-color:#ffff99; height:600px; width:15%; float:left;}
div#content   {background-color:#EEEEEE; height:600px; width:85%; float:left;}
div#footer    {background-color:#99bbbb; clear:both; text-align:center;}
h1 {margin-bottom:0;}
h2 {margin-bottom:0; font-size:14px;}
ul {margin:0;}
li {list-style:none;}
</style>
<script type="text/javascript" src="js/manage_school.js" charset="UTF-8"></script>
</head>
<body>
<%
if (session.getAttribute("username") == null) {
  response.sendRedirect("/futurestar/login.jsp");
} else {
%>
<div id="container">
<div id="header">
<h1>Main Title of Web Page</h1>
</div>
<div id="menu">
<h2>菜单</h2>
<ul>
<li><a href="manage_school.jsp">学校管理</a></li>
<li><a href="">教师管理</a></li>
<li><a href="">学生管理</a></li>
</ul>
</div>
<div id="content">
<input id="button_create" type="button" value="新建" onclick="on_button_create()">
<input id="button_search" type="button" value="查找" onclick="on_button_search()"><br/>
<span id="span_content"></span>
</div>
<div id="footer">Copyright FutureStar.com.cn</div>
</div>
<%
} // else
%>
</body>
</html>