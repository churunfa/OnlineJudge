<%@ page import="OnlineJudge.dao.impl.UserDaoImpl" %>
<%@ page import="OnlineJudge.domain.BugInfo" %>
<%@ page import="java.util.List" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>bug反馈</title>
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="./js/BUG.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="icon" type="image/png" sizes="144x144" href="../imgs/logo_blue_144.png"/>
    <script src="http://cdn.bootcss.com/mathjax/2.7.0/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
    <script type="text/x-mathjax-config">
    MathJax.Hub.Config({
        tex2jax: {inlineMath: [['$', '$']]},
        messageStyle: "none"
    });
    </script>
    <link rel="stylesheet" href="./css/BUG.css">
</head>
<body style="padding-top: 60px;
    min-width: 800px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);"  hidden>

<div id="header"></div>
<div class="main container">
    <h2 style="text-align: center">BUG反馈区</h2>
    <div style="margin-bottom: 30px;">
        <textarea disabled class="form-control" id="show" rows="20">
            <%
                UserDaoImpl userDao = new UserDaoImpl();
                List<BugInfo> bugInfos = userDao.fineBug();
                request.setAttribute("bugs",bugInfos);
            %>

            <c:forEach items="${bugs}" var="bug">
                [${bug.time}] ${bug.text}
            </c:forEach>

        </textarea>
    </div>
    <div>
        <textarea rows="4" id="comment" class="form-control autofit col-md-9" maxlength="2000";></textarea>
        <a id="submit">提交</a>
    </div>
</div>
<div id="footer"></div>
</body>
</html>