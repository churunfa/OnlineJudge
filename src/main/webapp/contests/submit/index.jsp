<%@ page import="OnlineJudge.dao.impl.ProblemDaoImpl" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="OnlineJudge.domain.Contest" %>
<%@ page import="java.util.List" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();

    String pid = request.getParameter("id");

    if(pid == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    Problem problem = problemDao.findProblemByPid(Integer.parseInt(pid));

    if(problem == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    request.setAttribute("pro",problem);

    Contest contest = contestsDao.findContestByCid(problem.getContest_id());

    request.setAttribute("contest",contest);

    List<Problem> problems = contestsDao.findProblemByCid(problem.getContest_id());

    request.setAttribute("pros",problems);

    List<String> languages = problemDao.findLanguages(problem.getPid());
    request.setAttribute("languages",languages);

%>


<!DOCTYPE html>
<html lang="en" style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Round #${contest.id}</title>
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="./js/submit.js"></script>
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
    <link rel="stylesheet" href="./css/submit.css">
</head>
<body style="padding-top: 60px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);
    min-width: 800px"  hidden>
<div id="header"></div>
<div hidden id="path">${pageContext.request.contextPath}</div>
<form class="main container">
    <div class="row">
        <div class="col-md-8">
            <select class="form-control" onclick="change_language()" id="problem_select">
                <c:forEach items="${pros}" var="p">
                    <c:if test="${p.pid == pro.pid}">
                        <option selected value="${p.pid}">${p.type}. ${p.title}</option>
                    </c:if>
                    <c:if test="${p.pid != pro.pid}">
                        <option value="${p.pid}">${p.type}. ${p.title}</option>
                    </c:if>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2">
            <select class="form-control" id="languages">
                <c:forEach items="${languages}" var="language" varStatus="sta">
                    <c:if test="${sta.index == 0}">
                        <option selected>${language}</option>
                    </c:if>
                    <c:if test="${sta.index != 0}">
                        <option>${language}</option>
                    </c:if>
                </c:forEach>
            </select>
        </div>
    </div>
<!--    background-color:rgba(255, 255, 255, 0);-->
    <pre style="padding: 0;margin-top: 20px;overflow: hidden;border-width: 0;background-color:rgba(255, 255, 255, 0); ">
        <textarea class="form-control"style="resize: none;padding: 20px" id="code" rows="20"></textarea>
    </pre>
    <button type="button" id="btn" data-loading-text="提交中..." class="btn btn-default" autocomplete="off">
        提交
    </button>
</form>
<div id="footer"></div>
</body>
</html>