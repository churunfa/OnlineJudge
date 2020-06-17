<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="OnlineJudge.domain.Contest" %>
<%@ page import="java.util.Date" %>
<%@ page import="OnlineJudge.domain.User_password" %>
<%@ page import="OnlineJudge.web.servlet.ContestServlet" %>
<%@ page import="OnlineJudge.service.impl.ContestsServiceImpl" %>
<%@ page import="OnlineJudge.domain.ProblemInfo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String id = request.getParameter("id");
    if(id == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }
    request.setAttribute("id",id);
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();

    Contest contestByCid = contestsDao.findContestByCid(Integer.parseInt(id));

    if(contestByCid == null){
        response.sendRedirect(request.getContextPath()+"/contests/wait?id="+id);
        return;
    }
    request.setAttribute("contest",contestByCid);
    request.setAttribute("title","Round #"+contestByCid.getId());

    long st = contestByCid.getStart_time().getTime();
    long now = new Date().getTime();
%>
<!DOCTYPE html>
<html lang="en" style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
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
    <link rel="stylesheet" href="css/contests_show.css">
    <script src="js/contests_show.js"></script>
    <script src="../../js/showdown.min.js"></script>
</head>
<body style="padding-top: 60px;
    min-width: 800px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);"  hidden>

<%
    User_password user = (User_password) request.getSession().getAttribute("User");

    if(user == null){
%>
    <script>
        alert("请先登录");
        location.href = "${pageContext.request.contextPath}/contests";
    </script>
<%
        return;
    }

    if(!"root".equals(user.getPower()) && user.getId() != contestByCid.getMaster()){
        if(now < st){
            response.sendRedirect(request.getContextPath()+"/contests/wait?id="+id);
            return;
        }
    }

    if("root".equals(user.getPower())  || user.getId() == contestByCid.getMaster()) request.setAttribute("flag",true);
    else request.setAttribute("flag",false);

    ContestsServiceImpl contestsService = new ContestsServiceImpl();
    List<ProblemInfo> problemInfos = contestsService.findProblemByCid(Integer.parseInt(id), user.getId());
    request.setAttribute("pros",problemInfos);
%>
<div hidden id="path">${pageContext.request.contextPath}</div>
<div id="header"></div>
<div class="container main">
    <div class="progress">
        <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuemin="0" id="progress" aria-valuemax="100" style="width: 0%;min-width: 2em;">
            <span class="">0%</span>
        </div>
    </div>
    <div class="row">
        <div class="col-md-9" >
            <div class="panel panel-default" id="left">
                <table class="table table-hover table-hover" id="pro">
                    <thead>
                        <!--                        glyphicon glyphicon-menu-up-->
                        <tr>
                            <th>#</th>
                            <th>标题</th>
                            <th>时间</th>
                            <th>空间</th>
                            <th>通过率</th>
                            <th>提交</th>
                            <c:if test="${flag}">
                                <th>编辑</th>
                                <th>删除</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${pros}" var="pro">
                            <tr id="pro1" style="
                                <c:if test="${pro.pos == 1}">
                                        background-color: #FFCCCC;
                                </c:if>
                                <c:if test="${pro.pos == 2}">
                                        background-color: #CCFFFF;
                                </c:if>
                            ">
                                <td style="display:table-cell; vertical-align:middle">${pro.problem.type}</td>
                                <td style="display:table-cell; vertical-align:middle">
                                    <a href="../problem_show?id=${pro.problem.pid}">${pro.problem.title}</a>
                                </td>
                                <td style="display:table-cell; vertical-align:middle">${pro.problem.time_limit}s</td>
                                <td style="display:table-cell; vertical-align:middle">${pro.problem.memory_limit}M</td>
                                <td style="display:table-cell; vertical-align:middle"><fmt:formatNumber type="number" value="${pro.rate}" maxFractionDigits="2"/>%(${pro.ac}/${pro.sum})</td>
                                <td style="display:table-cell; vertical-align:middle" ><a href="../submit?id=${pro.problem.pid}" class="glyphicon glyphicon-level-up"></a></td>
                                <c:if test="${flag}">
                                    <td style="display:table-cell; vertical-align:middle" ><a href="../problem_add?pid=${pro.problem.pid}" class="glyphicon glyphicon-pencil"></a></td>
                                    <td style="display:table-cell; vertical-align:middle" ><a href="${pageContext.request.contextPath}/contestServlet/delProblem?id=${pro.problem.pid}" class="glyphicon glyphicon-trash"></a></td>
                                </c:if>
                            </tr>
                        </c:forEach>
<%--                        <tr id="pro1" style="background: #0dd2ef">--%>
<%--                            <td style="display:table-cell; vertical-align:middle">1</td>--%>
<%--                            <td style="display:table-cell; vertical-align:middle">--%>
<%--                                <a href="../problem_show">A+B问题</a>--%>
<%--                            </td>--%>
<%--                            <td style="display:table-cell; vertical-align:middle">1s,52M</td>--%>
<%--                            <td style="display:table-cell; vertical-align:middle">100%(1/1)</td>--%>
<%--                            <td style="display:table-cell; vertical-align:middle" ><a href="../submit" class="glyphicon glyphicon-level-up"></a></td>--%>
<%--                            <td style="display:table-cell; vertical-align:middle" ><a href="../problem_add" class="glyphicon glyphicon-pencil"></a></td>--%>
<%--                            <td style="display:table-cell; vertical-align:middle" ><a href="#" class="glyphicon glyphicon-trash"></a></td>--%>
<%--                        </tr>--%>
                    </tbody>
                </table>
            </div>
            <c:if test="${flag}">
                <a href="../problem_add?cid=${id}" class="btn btn-default glyphicon glyphicon-plus" type="submit" id="add"></a>
            </c:if>
        </div>
        <div class="col-md-3">
            <div id="mess">
                <p style="text-align: center;padding-top: 10px;">通知</p>
                <div style="padding: 10px;" id="tz"></div>
            </div>
            <div style="margin-top: 30px;padding-left: 50px">
                <a class="" id="sum" href="../status?cid=${id}">所有提交</a>
                <a class="" id="contests_rank" href="../rank?id=${id}">查看排名</a>
            </div>
        </div>
    </div>
    <c:if test="${sessionScope.User.power == 'root' || sessionScope.User.id == contest.master }">
        <div style="position: relative;min-width: 750px;overflow: hidden">
            <textarea rows="4" class="form-control autofit col-md-9" maxlength="2000"; id="news"></textarea>
            <a class="submit" id="update">更新通知内容</a>
        </div>
    </c:if>
</div>

<textarea hidden id="old_tz"></textarea>

<!-- Button trigger modal -->
<button id="tz_btn" style="display: none" type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
    通知
</button>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">有新通知</h4>
            </div>
            <div class="modal-body" id="tongzhi"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<div id="footer"></div>
</body>
</html>