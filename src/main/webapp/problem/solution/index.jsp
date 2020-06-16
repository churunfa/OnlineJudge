<%@ page import="OnlineJudge.service.impl.BlogServiceImpl" %>
<%@ page import="OnlineJudge.domain.SolutionInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="OnlineJudge.dao.impl.ProblemDaoImpl" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@ page import="OnlineJudge.domain.User" %>
<%@ page import="OnlineJudge.domain.User_password" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en"  style="min-width: 800px;">
<%
    String pids = request.getParameter("pid");
    if(pids == null ){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    int pid = Integer.parseInt(pids);

    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    Problem pro = problemDao.findProblemByPid(pid);
    if(pro == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    User_password user = (User_password) request.getSession().getAttribute("User");

    int uid = user == null ? 0 :user.getId();

    BlogServiceImpl blogService = new BlogServiceImpl();
    List<SolutionInfo> solutionInfo = blogService.findSolutionInfoBy(pid,uid);
    if(!pro.isIs_show()){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }
    request.setAttribute("info",solutionInfo);
    request.setAttribute("pro",pro);
%>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${pro.title}题解</title>
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="js/solution.js"></script>
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
    <link rel="stylesheet" href="./css/solution.css">
</head>
<body style="padding-top: 60px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);
    min-width: 800px"  hidden>

<div id="header"></div>
    <div class="main container">
        <h1 style="text-align: center"><a href="../content?pid=${pro.pid}">${pro.title}</a></h1>
        <a class="" id="add" href="./create?pid=${pro.pid}"> 创建题解</a>
        <c:forEach items="${info}" var="data">
        <div class="panel panel-default sol-css">
                <div class="panel-heading row sol-css">
                    <div class="col-md-3 left">
                        <a href="${pageContext.request.contextPath}/user?id=${data.user.id}" title="${data.user.name}">
                            <img src="${pageContext.request.contextPath}${data.user.head_img}" alt="头像" class="img-circle user-img" style="width: 60px">
                        </a>
                        <div>
                            <span style="padding-left: 20px;" class="glyphicon glyphicon-heart fabulous"></span>
                            <span>${data.solution.love}</span>
                        </div>
                        <div>
                            <span>创建于<fmt:formatDate value="${data.solution.create_time}" pattern="yyyy年MM月dd日"/></span>
                        </div>
                    </div>
                    <div class="col-md-7 right">
                        <a href="./desc?id=${data.solution.id}">${data.solution.title}<c:if test="${data.solution.show == false}">（未提交）</c:if></a>

                    </div>
                    <c:if test="${data.user.id == sessionScope.User.id}">
                        <div class="col-md-2 change">
                            <a href="./create?pid=${pro.pid}&id=${data.solution.id}">修改</a>
                            <a class="del" href="${pageContext.request.contextPath}/blogServlet/delServlet?pid=${pro.pid}&id=${data.solution.id}" id="del">删除</a>
                        </div>
                    </c:if>
                </div>
        </div>
        </c:forEach>
    </div>
<div id="footer"></div>
</body>
</html>