<%@ page import="OnlineJudge.service.impl.ContestsServiceImpl" %>
<%@ page import="OnlineJudge.domain.ContestInfo" %>
<%@ page import="OnlineJudge.domain.PageBean" %>
<%@ page import="OnlineJudge.domain.User" %>
<%@ page import="java.util.Date" %>
<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="OnlineJudge.domain.User_password" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en" style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>比赛</title>
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="./js/contests.js"></script>
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
    <link rel="stylesheet" href="./css/contests.css">
</head>
<body style="padding-top: 60px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);
    min-width: 800px"  hidden>

<%
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    int pg=1,size=10,uid = 0;

    String pg1 = request.getParameter("pg");
    String size1 = request.getParameter("size");
    User_password user = (User_password) request.getSession().getAttribute("User");

    if(user != null) uid = user.getId();

    if(pg1 != null ) pg = Integer.parseInt(pg1);
    if(size1 != null) size = Integer.parseInt(size1);

    ContestsServiceImpl contestsService = new ContestsServiceImpl();

    PageBean<ContestInfo> contestInfo = contestsService.findContestInfo(pg, size,uid);

    request.setAttribute("uid",uid);
    request.setAttribute("pg",pg);
    request.setAttribute("size",size);
    request.setAttribute("info",contestInfo.getList());
    request.setAttribute("max",contestInfo.getTotalPage());

    List<Integer> checks = new ArrayList<Integer>();
    for(ContestInfo con:contestInfo.getList()){
        if(user != null)  checks.add(contestsDao.checkSign(uid,con.getContest().getId()));
        else checks.add(0);
    }

    int st=Math.max(contestInfo.getCurrentPage()-2,1);
    int ed = st;
    for(int i=0;i<5;i++){
        if(st+i>contestInfo.getTotalPage()) break;
        ed=st+i;
    }

    while(ed-st+1<4){
        if(st==1) break;
        st--;
    }

    request.setAttribute("st",st);
    request.setAttribute("ed",ed);
    request.setAttribute("flag",checks);
%>

<div id="header"></div>
<div id="main">
    <div class="container-fluid ">
        <div class="col-md-10 left">
            <div class="panel-body" style="background-color: #EDEDED;">
                <ul class="list-group">
                    <div class="panel panel-default" style="background-color: #EBEBEB;">
                        <!-- Table -->
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>比赛名称</th>
                                    <th style="min-width: 80px">出题人</th>
                                    <th>开始时间</th>
                                    <th>比赛时长</th>
                                    <th>状态</th>
                                    <th>&nbsp;</th>

                                </tr>
                            </thead>
                            <tbody style="text-align: center">
                            <c:forEach items="${info}" var="data" varStatus="sta">
                                <tr>
                                    <td style="display:table-cell; vertical-align:middle;max-width: 200px"><a href="${pageContext.request.contextPath}/contests/contests_show?id=${data.contest.id}">Round #${data.contest.id} ${data.contest.name}</a></td>
                                    <td style="display:table-cell; vertical-align:middle;max-width: 150px"><img src="${pageContext.request.contextPath}${data.user.head_img}" alt="头像" class="img-circle" style="width: 30px;height: 30px"><a href="${pageContext.request.contextPath}/user?id=${data.user.id}" style="font-size: 20px;display: block">${data.user.name}</a></td>
                                    <td style="display:table-cell; vertical-align:middle"><fmt:formatDate value="${data.contest.start_time}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                                    <td style="display:table-cell; vertical-align:middle">${data.time}</td>
                                    <td style="display:table-cell; vertical-align:middle">
                                        <span>${data.sta}</span>
                                    </td>
                                    <td style="display:table-cell; vertical-align:middle">
                                        <c:if test="${sessionScope.User.id == data.user.id || sessionScope.User.power=='root'}">
                                            <a href="${pageContext.request.contextPath}/contests/create?id=${data.contest.id}">点击编辑</a>
                                        </c:if>
                                        <c:if test="${data.type == 1}">
                                            <c:if test="${sessionScope.User.id != data.user.id && sessionScope.User.power!='root'}">
                                                <c:if test="${flag[sta.index] != 0}"><div>已报名</div></c:if>
                                                <c:if test="${flag[sta.index] == 0}">
                                                    <a href="${pageContext.request.contextPath}/contests/sign?id=${data.contest.id}">点击报名</a><br>
                                                </c:if>
<%--                                                <a href="${pageContext.request.contextPath}/contests/sign?id=${data.contest.id}" onclick="fsign(${data.contest.id})">点击报名</a><br>--%>
                                            </c:if>
                                            <a href="./signlist?id=${data.contest.id}" >报名名单</a>
                                        </c:if>
                                        <c:if test="${data.type == 2}">
                                            <a href="${pageContext.request.contextPath}/contests/contests_show?id=${data.contest.id}">点击进入</a><br>
                                            <a href="./signlist?id=${data.contest.id}" >报名名单</a>
                                        </c:if>
                                        <c:if test="${data.type == 3}">
                                            <a href="${pageContext.request.contextPath}/contests/rank?id=${data.contest.id}">临时排名</a>
                                            <div>
                                                解决${data.acSum}，共${data.contest.sum}题
                                            </div>
                                        </c:if>
                                        <c:if test="${data.type == 4}">
                                            <a href="${pageContext.request.contextPath}/contests/rank?id=${data.contest.id}">最终排名</a>
                                            <c:if test="${uid != 0}">
                                                <div>
                                                    解决${data.acSum}，共${data.contest.sum}题
                                                </div>
                                            </c:if>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </ul>
            </div>
            <nav style="text-align: center" aria-label="Page navigation">
                <ul class="pagination">
                    <li>
                        <a href="${pageContext.request.contextPath}/contests?size=${size}&tag=${tag}" aria-label="first">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <c:forEach begin="${st}" end="${ed}" var="i">
                        <li><a href="${pageContext.request.contextPath}/contests?pg=${i}&size=${size}">${i}</a></li>
                    </c:forEach>

                    <%--                    <li><a href="#">2</a></li>--%>
                    <%--                    <li><a href="#">3</a></li>--%>
                    <%--                    <li><a href="#">4</a></li>--%>
                    <%--                    <li><a href="#">5</a></li>--%>
                    <li>
                        <a href="${pageContext.request.contextPath}/contests?pg=${max}&size=${size}" aria-label="end">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                    <li>
                        <form class="navbar-form navbar-left" id="change_page">
                            <div class="form-group">
                                <input type="text" class="form-control" id="page_key" placeholder="页码">
                                <a class="btn btn-default glyphicon glyphicon-share-alt" id="page_btn"></a>
                            </div>
                        </form>
                    </li>
                </ul>
            </nav>
        </div>
        <div class="col-md-2 right">
            <a id="new" href="./create">创办比赛</a>
            <a id="info" href="./introduce">比赛介绍</a>
            <div id="notice">
                <p style="padding:20px">比赛说明(
                    <c:if test="${info.size() > 0 && info[0].type != 4 && info[0].type != 3}">Rank #${info[0].contest.id}</c:if>
                    <c:if test="${info.size() == 0 || info[0].type == 4 || info[0].type == 3}">暂无比赛。。</c:if>
                    ）：</p>
                <p style="padding:0px 20px 20px 20px">
                    <c:if test="${info.size() > 0 && info[0].type != 4 && info[0].type != 3}">${info[0].contest.notice}</c:if>
                </p>
            </div>
        </div>
    </div>
</div>
<div id="footer"></div>
<div hidden id="maxPg">${max}</div>
<div hidden id="success">
    <span>报名成功</span><br>
</div>
</body>
</html>