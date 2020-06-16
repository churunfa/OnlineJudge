<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="OnlineJudge.domain.Contest" %>
<%@ page import="OnlineJudge.service.impl.ContestsServiceImpl" %>
<%@ page import="OnlineJudge.domain.ContestRank" %>
<%@ page import="java.util.List" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en" style="min-width: 800px;">

<%
    String id = request.getParameter("id");
    if(id == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    Contest contest = contestsDao.findContestByCid(Integer.parseInt(id));

    if(contest == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    request.setAttribute("contest",contest);

%>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Round #${contest.id} ${contest.name}</title>
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
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
    <link rel="stylesheet" href="./css/rank.css">
    <script src="js/rank.js"></script>
</head>
<body style="padding-top: 60px;
    min-width: 800px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);"  hidden>
<%
    ContestsServiceImpl contestsService = new ContestsServiceImpl();

    List<Problem> problem = contestsDao.findProblemByCid(Integer.parseInt(id));

    List<ContestRank> rank = contestsService.findRank(Integer.parseInt(id));
    request.setAttribute("pro",problem);
    request.setAttribute("user",rank);
%>
<div id="header"></div>
<div class="container-fluid" style="min-height: 500px">
    <div class="container-fluid main">
    <div hidden id="cid">${contest.id}</div>
    <div hidden id="path">${pageContext.request.contextPath}</div>
    <div class="progress">
        <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuemin="0" id="progress" aria-valuemax="100" style="width: 0%;min-width: 2em;">
            <span class="">0%</span>
        </div>
    </div>
    <table class="table table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>参与者</th>
            <th>解决</th>
            <c:forEach items="${pro}" var="p">
                <th>${p.type} - ${p.title}</th>
            </c:forEach>
            <th>总罚时</th>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <tr>
            <c:forEach items="${user}" var="info" varStatus="sta">
                <td style="display:table-cell; vertical-align:middle;font-size: 20px">${info.rank}</td>
                <td style="display:table-cell; vertical-align:middle"><img src="${pageContext.request.contextPath}${info.user.head_img}" alt="头像" class="img-circle" style="width: 30px"><a href="${pageContext.request.contextPath}/user?id=${info.user.id}" style="font-size: 20px;display: block">${info.user.name}</a></td>
                <td style="display:table-cell; vertical-align:middle">${info.sum}</td>
                <c:forEach items="${pro}" var="p" varStatus="sta">
                    <td style="display:table-cell; vertical-align:middle; border:1px #fff solid;"
                        <c:if test="${info.info[p.pid].acFlag && !info.info[p.pid].first}">
                            class="pass"
                        </c:if>
                        <c:if test="${info.info[p.pid].sum != 0 && !info.info[p.pid].acFlag}">
                            class="unpass"
                        </c:if>
                        <c:if test="${info.info[p.pid].first}">
                            class="first_pass"
                        </c:if>
                    >
<%--                            <span>${info.info[p.pid].penalty}</span><br>--%>
                        <span>
                             <fmt:formatNumber type="number" value="${(info.info[p.pid].penalty-info.info[p.pid].penalty%(60*60))/(60*60)}" maxFractionDigits="0" pattern="00"/>:<fmt:formatNumber type="number" value="${(info.info[p.pid].penalty-info.info[p.pid].penalty%60)/60%60}" maxFractionDigits="0" pattern="00"/>:<fmt:formatNumber type="number" value="${info.info[p.pid].penalty%60}" maxFractionDigits="0" pattern="00"/><br>
                        <c:if test="${info.info[p.pid].acFlag}">
                            <c:if test="${info.info[p.pid].sum != 1}">
                                <span>-${info.info[p.pid].sum-1}</span>
                            </c:if>

                        </c:if>
                 </td>
            </c:forEach>
                    <td style="display:table-cell; vertical-align:middle;">
                        <fmt:formatNumber type="number" value="${(info.penalty-info.penalty%(60*60))/(60*60)}" maxFractionDigits="0" pattern="00"/>:<fmt:formatNumber type="number" value="${(info.penalty-info.penalty%60)/60%60}" maxFractionDigits="0" pattern="00"/>:<fmt:formatNumber type="number" value="${info.penalty%60}" maxFractionDigits="0" pattern="00"/><br>
                    </td>
<%--                    <td style="display:table-cell; vertical-align:middle;" class="pass">--%>
<%--                        <span>1:40:30</span><br>--%>
<%--                        <span>(-1)</span>--%>
<%--                    </td>--%>
<%--                    <td style="display:table-cell; vertical-align:middle;" class="unpass">--%>
<%--                        <span></span>--%>
<%--                        <span>(-11)</span>--%>
<%--                    </td>--%>
<%--                    <td style="display:table-cell; vertical-align:middle" class="first_pass">--%>
<%--                        <span>1:40:30</span><br>--%>
<%--                        <span>(-1)</span>--%>
<%--                    </td>--%>
<%--                    <td style="display:table-cell; vertical-align:middle" class="pass">--%>
<%--                        <span>1:40:30</span><br>--%>
<%--                        <span>(-1)</span>--%>
<%--                    </td>--%>
<%--                    <td style="display:table-cell; vertical-align:middle" class="pass">--%>
<%--                        <span>1:40:30</span><br>--%>
<%--                        <span>(-1)</span>--%>
<%--                    </td>--%>
<%--                    <td style="display:table-cell; vertical-align:middle">--%>
<%--                        <span>11:30:50</span>--%>
<%--                    </td>--%>
                </tr>
            </c:forEach>
<%--        <tr>--%>
<%--            <td style="display:table-cell; vertical-align:middle">1</td>--%>
<%--            <td style="display:table-cell; vertical-align:middle"><img src="../../imgs/crf.png" alt="头像" class="img-circle" style="width: 30px"><a href="#" style="font-size: 20px;display: block">褚润发</a></td>--%>
<%--            <td style="display:table-cell; vertical-align:middle">3</td>--%>
<%--            <td style="display:table-cell; vertical-align:middle;" class="pass">--%>
<%--                <span>1:40:30</span><br>--%>
<%--                <span>(-1)</span>--%>
<%--            </td>--%>
<%--            <td style="display:table-cell; vertical-align:middle;" class="unpass">--%>
<%--                <span></span>--%>
<%--                <span>(-11)</span>--%>
<%--            </td>--%>
<%--            <td style="display:table-cell; vertical-align:middle" class="first_pass">--%>
<%--                <span>1:40:30</span><br>--%>
<%--                <span>(-1)</span>--%>
<%--            </td>--%>
<%--            <td style="display:table-cell; vertical-align:middle" class="pass">--%>
<%--                <span>1:40:30</span><br>--%>
<%--                <span>(-1)</span>--%>
<%--            </td>--%>
<%--            <td style="display:table-cell; vertical-align:middle" class="pass">--%>
<%--                <span>1:40:30</span><br>--%>
<%--                <span>(-1)</span>--%>
<%--            </td>--%>
<%--            <td style="display:table-cell; vertical-align:middle">--%>
<%--                <span>11:30:50</span>--%>
<%--            </td>--%>
<%--        </tr>--%>
        </tbody>
    </table>
</div>
</div>
<div id="footer"></div>
</body>
</html>