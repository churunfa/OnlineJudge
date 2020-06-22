<%--
  Created by IntelliJ IDEA.
  User: crf
  Date: 2020/6/13
  Time: 下午8:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="icon" type="image/png" sizes="144x144" href="./imgs/logo_blue_144.png"/>
</head>
<body>
    <a href="${pageContext.request.contextPath}/quartz/showProblem?id=51">添加</a>
    <a href="${pageContext.request.contextPath}/quartz/reTest?id=51">重测</a>
    <a href="${pageContext.request.contextPath}/quartz/remove?id=51">移除</a>
    <a href="${pageContext.request.contextPath}/quartz/removeDelJob">移除delJob</a>
    <a href="${pageContext.request.contextPath}/quartz/rantingChange?id=1">更新ranting</a>
</body>
</html>
