<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
<!--
	Author: W3layouts
	Author URL: http://w3layouts.com
	License: Creative Commons Attribution 3.0 Unported
	License URL: http://creativecommons.org/licenses/by/3.0/
-->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Error</title>
    <!-- Meta tag Keywords -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="keywords" content="Fantasy Error Page Responsive Widget,Login form widgets, Sign up Web forms , Login signup Responsive web form,Flat Pricing table,Flat Drop downs,Registration Forms,News letter Forms,Elements" />
    <script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
    function hideURLbar(){ window.scrollTo(0,1); } </script>
    <!-- Meta tag Keywords -->
    <!-- css files -->
    <link href="${pageContext.request.contextPath}/error/css/style.css" rel="stylesheet" type="text/css" media="all">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/error/css/font-awesome.css"> <!-- Font-Awesome-Icons-CSS -->
    <link rel="icon" type="image/png" sizes="144x144" href="${pageContext.request.contextPath}/imgs/logo_blue_144.png"/>
    <!-- //css files -->
    <!-- online-fonts -->
    <!--//online-fonts -->
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/error/js/error.js"></script>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</head>

<body>
<div id="header1"></div>
<div class="header">
    <h1>error page</h1>
</div>
<div class="w3-main">
    <div class="agile-info">
        <h2>404</h2>
        <h3>SORRY</h3>
        <p>The Page You're Looking for Was Not Found.</p>
        <a href="${pageContext.request.contextPath}/"><i class="fa fa-angle-double-left" aria-hidden="true"></i>go back</a>
    </div>
</div>
<div id="footer"></div>
<div hidden id="path">${pageContext.request.contextPath}</div>
</body>
</html>