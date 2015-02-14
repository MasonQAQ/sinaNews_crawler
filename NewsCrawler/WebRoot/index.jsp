<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>

<%@page import="com.lxf.dao.imp.NewsDao"%>
<%@page import="com.lxf.dao.inf.NewsDaoInf"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	NewsDaoInf ni = new NewsDao();
	request.setAttribute("newsList", ni.query());
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>newsCrawler_lxf</title>
<%-- ������Զ�ˢ��һ�� --%>
<meta http-equiv="refresh" content="300">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
a:link,a:visited {
	color: #000000;
	text-decoration: none;
}

a:hover,a:active {
	color: red;
	text-decoration: none;
}

li {
	padding: 5px;
	font-size: 15px;
}
</style>
</head>

<body>
	<div align="center">
		<form action="NewsCrawlerServlet" method="post">
			<input type="hidden" name="op"
				value="${sessionScope.op==null?'start':sessionScope.op}" /> <input
				type="submit"
				value="${sessionScope.op=='stop'?'ֹͣ�Զ�ץȡ���Ź���':'��ʼ�Զ�ץȡ���Ź���'}" />
		</form>
	</div>
	<ul>
		<c:forEach items="${requestScope.newsList}" var="news">
			<li>&lt;${news.type}&gt;<a href="NewsServlet?id=${news.id}">${news.title}</a>[${news.newsdate}]
			</li>
		</c:forEach>
	</ul>
</body>
</html>
