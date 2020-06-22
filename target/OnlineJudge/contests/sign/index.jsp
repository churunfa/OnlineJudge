<%@ page import="OnlineJudge.service.impl.ContestsServiceImpl" %>
<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="OnlineJudge.domain.Contest" %>
<%@ page import="java.util.Date" %>
<%@ page import="OnlineJudge.domain.User_password" %><%--
  Created by IntelliJ IDEA.
  User: churunfa
  Date: 2020/5/19
  Time: 下午 4:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en" style="min-height: 100%;min-width: 800px">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>报名</title>
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="./js/sign.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="icon" type="image/png" sizes="144x144" href="../../imgs/logo_blue_144.png"/>
    <script src="http://cdn.bootcss.com/mathjax/2.7.0/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
    <script type="text/x-mathjax-config">
    MathJax.Hub.Config({
        tex2jax: {inlineMath: [['$', '$']]},
        messageStyle: "none"
    });
    </script>
</head>

<%
    String id = request.getParameter("id");
    if(id == null ){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    Contest contest = contestsDao.findContestByCid(Integer.parseInt(id));
    if(contest == null ){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    long st = contest.getStart_time().getTime();
    long now = new Date().getTime();

    if(now > st){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    User_password user = (User_password) request.getSession().getAttribute("User");
    if(user == null) request.setAttribute("lv",-1);
    else request.setAttribute("lv",user.getLv());

    request.setAttribute("con",contest);
    request.setAttribute("contest",contest);

%>

<body style="padding-top: 60px;
    min-height: 100%;
    min-width: 800px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);"  hidden>
<div id="header"></div>
<div class="container" style="margin-top: 50px">
    <div class="jumbotron">
        <h1>Round #${con.id} ${con.name}</h1>
        <p>此类比赛通常会在比赛结束后按照排名计算用户评分</p>
        <p>报名者务必要定时参加，以免扣分</p>
        <p>不报名也可以参加比赛，比赛结束后不会计算评分</p>
        <p>比赛结束后将公开所有提交代码，同时公开hank</p>
        <p>注意：一旦报名，您将无法取消！</p>
        <p><a class="btn btn-primary btn-lg" role="button" id="sign">报名</a></p>
    </div>
</div>
<div hidden id="id">${con.id}</div>
<div hidden id="div">${contest.type}</div>
<div hidden id="path">${pageContext.request.contextPath}</div>
<div hidden id="lv">${lv}</div>
<div id="footer"></div>
</body>
</html>