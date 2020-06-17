<%@ page import="OnlineJudge.dao.impl.UserDaoImpl" %>
<%@ page import="java.util.List" %>
<%@ page import="OnlineJudge.service.UserService" %>
<%@ page import="OnlineJudge.service.impl.UserServiceImpl" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="OnlineJudge.util.TimeSub" %>
<%@ page import="java.util.Collections" %>
<%@ page import="OnlineJudge.domain.*" %>
<%@ page import="OnlineJudge.dao.ContestsDao" %>
<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en"  style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Online Judge</title>
    <!-- Bootstrap -->
    <link href="./css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="./js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="./js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="icon" type="image/png" sizes="144x144" href="./imgs/logo_blue_144.png"/>
    <link rel="stylesheet" href="./css/home.css">
    <script src="./js/home.js"></script>

    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>

<body hidden style="min-width: 800px;">
<%
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    UserService userService = new UserServiceImpl();
    UserDaoImpl userDao = new UserDaoImpl();
    List<ContestAndMaster> contests = userService.findNotEndContestAndMaster();
    request.setAttribute("contests",contests);
    List<String> lengths = new ArrayList<String>();
    List<String> dates = new ArrayList<String>();
    String run = "比赛正在进行";

    User_password user = (User_password) request.getSession().getAttribute("User");

    List<Integer> checks = new ArrayList<Integer>();

    for(ContestAndMaster contest:contests){
        lengths.add(TimeSub.Sub(contest.getContest().getEnd_time(), contest.getContest().getStart_time()));
        Date date = new Date();
        if(contest.getContest().getStart_time().getTime() - date.getTime() < 0){
            dates.add(run);
        }else{
            dates.add("距比赛开始还有："+TimeSub.Sub(contest.getContest().getStart_time(),date));
        }

        if(user != null && user.getId() != contest.getMaster().getId() &&!"root".equals(user.getPower()))checks.add(contestsDao.checkSign(user.getId(),contest.getContest().getId()));

    }

    request.setAttribute("flags",checks);
    request.setAttribute("lengths",lengths);
    request.setAttribute("dates",dates);
    request.setAttribute("run",run);
%>
<%

    List<Top3> top3s = userService.findTop3();
    Collections.reverse(top3s);

    request.setAttribute("top3",top3s);

    List<top3Ac> info = new ArrayList<top3Ac>() ;

    for(Top3 top:top3s){
        Contest_User_info top1_info = null,top2_info = null,top3_info = null;
        if(top.getTop1() != null) top1_info = userDao.findContest_User_info(top.getContest().getId(), top.getTop1().getId());
        if(top.getTop2() != null) top2_info = userDao.findContest_User_info(top.getContest().getId(), top.getTop2().getId());
        if(top.getTop3() != null) top3_info = userDao.findContest_User_info(top.getContest().getId(), top.getTop3().getId());
//        System.out.println( top1_info.getContest_id()+"--"+ top1_info.getAc_sum());
        top3Ac ac = new top3Ac();
        if(top1_info != null ) ac.setTop1(top1_info.getAc_sum());
        if(top2_info != null ) ac.setTop2(top2_info.getAc_sum());
        if(top3_info != null ) ac.setTop3(top3_info.getAc_sum());
        info.add(ac);
    }
    request.setAttribute("ac_sum",info);
//    System.out.println(info.size());
//    for(top3Ac inf : info) System.out.println(inf.top1);
//    System.out.println(info.get(0).top1+"---"+info.get(0).top2+"---"+info.get(0).top3);
%>

<div id="header"></div>
<div id="bd">
        <div class="panel panel-info">
            <div class="panel-heading">近期比赛</div>
            <div class="panel-body" style="background-color: #EDEDED;">
                <ul class="list-group">
                    <div class="panel panel-default" style="background-color: #EBEBEB;">
                        <!-- Table -->
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>比赛名称</th>
                                    <th>比赛类型</th>
                                    <th>出题人</th>
                                    <th>开始时间</th>
                                    <th>比赛时长</th>
                                    <th>&nbsp;</th>
                                    <th>&nbsp;</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${contests}" var="contest" varStatus="sta">
                                <tr>
                                    <td style="display:table-cell; vertical-align:middle"><a href="${pageContext.request.contextPath}/contests/contests_show/?id=${contest.contest.id}">Round #${contest.contest.id} ${contest.contest.name}</a></td>
                                    <td style="display:table-cell; vertical-align:middle">${contest.contest.type}</td>
                                    <td style="display:table-cell; vertical-align:middle"><img src="${pageContext.request.contextPath}${contest.master.head_img}" alt="头像" class="img-circle" style="width: 30px;height: 30px"><a href="${pageContext.request.contextPath}/user?id=${contest.master.id}" style="font-size: 20px;display: block">${contest.master.name}</a></td>
                                    <td style="display:table-cell; vertical-align:middle"><fmt:formatDate value="${contest.contest.start_time}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                                    <td style="display:table-cell; vertical-align:middle">${lengths[sta.index]}</td>
                                    <td style="display:table-cell; vertical-align:middle">${dates[sta.index]}</td>
                                    <td style="display:table-cell; vertical-align:middle">
                                        <c:if test="${dates[sta.index] != run}">
                                            <c:if test="${flags[sta.index] == 0}">
                                                <a href="${pageContext.request.contextPath}/contests/sign/?id=${contest.contest.id}">点击报名</a>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${dates[sta.index] == run}">
                                            <a href="${pageContext.request.contextPath}/contests/contests_show/?id=${contest.contest.id}">点击进入</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>

                        </table>
                    </div>
                    <span hidden>最近没有比赛~ <a href="${pageContext.request.contextPath}/contests/create">点我可以创建比赛~</a></span>
                </ul>
            </div>
        </div>
        <div id="top">
                <!-- Default panel contents -->
<!--                <div class="panel-heading">TOP 1</div>-->

                <!-- Table -->
                <table class="table table-bordered table-hover" style="background-color: #F2F2F2;">
                    <thead>
                        <tr>
                            <th></th>
                            <th colspan="3" class="top1_col">TOP 1</th>
                            <th colspan="3" class="top2_col">TOP 2</th>
                            <th colspan="3" class="top3_col">TOP 3</th>
                        </tr>
                    </thead>
                    <thead>
                        <tr>
                            <th>场次</th>
                            <th class="top1_col">用户</th>
                            <th class="top1_col">AC数</th>
                            <th class="top1_col">rating</th>
                            <th class="top2_col">用户</th>
                            <th class="top2_col">AC数</th>
                            <th class="top2_col">rating</th>
                            <th class="top3_col">用户</th>
                            <th class="top3_col">AC数</th>
                            <th class="top3_col">rating</th>
                        </tr>
                    </thead>
                    <tbody>
<%--                    <c:if test="${fn:length(top3)>10}">--%>
<%--                        <c:forEach begin="0" end="9" var="i">--%>
<%--                            <tr>--%>
<%--                                <td style="display:table-cell; vertical-align:middle"><a href="${pageContext.request.contextPath}/contests/contests_show/?id=${top3[i].contest.id}">Round #${top3[i].contest.id} ${top3[sta.index].contest.name}</a></td>--%>
<%--                                <c:if test="${not empty top3[i].top1}">--%>
<%--                                    <td style="display:table-cell; vertical-align:middle" class="top1_col"><img src="${pageContext.request.contextPath}/${top3[i].top1.head_img}" alt="头像" class="img-circle" style="width: 30px"><a href="${pageContext.request.contextPath}/user?id=${top3[i].top1.id}" style="font-size: 20px;display: block">${top3[i].top1.name}</a></td>--%>
<%--                                    <td style="display:table-cell; vertical-align:middle" class="top1_col">${ac_sum[i].top1}</td>--%>
<%--                                    <td style="display:table-cell; vertical-align:middle" class="top1_col">${top3[i].top1.ranting}</td>--%>
<%--                                </c:if>--%>
<%--                                <c:if test="${empty top3[i].top1}"><td></td><td></td><td></td></c:if>--%>
<%--                                <c:if test="${not empty top3[i].top2}">--%>
<%--                                    <td style="display:table-cell; vertical-align:middle" class="top2_col"><img src="${pageContext.request.contextPath}/${top3[i].top2.head_img}" alt="头像" class="img-circle" style="width: 30px"><a href="${pageContext.request.contextPath}/user?id=${top3[i].top2.id}" style="font-size: 20px;display: block">${top3[i].top2.name}</a></td>--%>
<%--                                    <td style="display:table-cell; vertical-align:middle" class="top1_col">${ac_sum[i].top2}</td>--%>
<%--                                    <td style="display:table-cell; vertical-align:middle" class="top1_col">${top3[i].top2.ranting}</td>--%>
<%--                                </c:if>--%>
<%--                                <c:if test="${empty top3[i].top2}"><td></td><td></td><td></td></c:if>--%>
<%--                                <c:if test="${not empty top3[i].top3}">--%>
<%--                                    <td style="display:table-cell; vertical-align:middle" class="top2_col"><img src="${pageContext.request.contextPath}/${top3[i].top3.head_img}" alt="头像" class="img-circle" style="width: 30px"><a href="${pageContext.request.contextPath}/user?id=${top3[i].top3.id}" style="font-size: 20px;display: block">${top3[i].top3.name}</a></td>--%>
<%--                                    <td style="display:table-cell; vertical-align:middle" class="top1_col">${ac_sum[i].top3}</td>--%>
<%--                                    <td style="display:table-cell; vertical-align:middle" class="top1_col">${top3[i].top3.ranting}</td>--%>
<%--                                </c:if>--%>
<%--                                <c:if test="${empty top3[i].top3}"><td></td><td></td><td></td></c:if>--%>
<%--                            </tr>--%>
<%--                        </c:forEach>--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${fn:length(top3)<=10}">--%>
                        <c:forEach items="${top3}" var="top" varStatus="sta">
                            <tr>
                                <td style="display:table-cell; vertical-align:middle"><a href="${pageContext.request.contextPath}/contests/contests_show/?id=${top3[sta.index].contest.id}">Round #${top3[sta.index].contest.id} ${top3[sta.index].contest.name}</a></td>
                                <c:if test="${not empty top3[sta.index].top1}">
                                    <td style="display:table-cell; vertical-align:middle" class="top1_col"><img src="${pageContext.request.contextPath}${top3[sta.index].top1.head_img}" alt="头像" class="img-circle" style="width: 30px;height: 30px"><a href="${pageContext.request.contextPath}/user?id=${top3[sta.index].top1.id}" style="font-size: 20px;display: block">${top3[sta.index].top1.name}</a></td>
                                    <td style="display:table-cell; vertical-align:middle" class="top1_col">${ac_sum[sta.index].top1}</td>
                                    <td style="display:table-cell; vertical-align:middle" class="top1_col">${top3[sta.index].top1.ranting}</td>
                                </c:if>
                                <c:if test="${empty top3[sta.index].top1}"><td></td><td></td><td></td></c:if>
                                <c:if test="${not empty top3[sta.index].top2}">
                                <td style="display:table-cell; vertical-align:middle" class="top2_col"><img src="${pageContext.request.contextPath}${top3[sta.index].top2.head_img}" alt="头像" class="img-circle" style="width: 30px;height: 30px"><a href="${pageContext.request.contextPath}/user?id=${top3[sta.index].top2.id}" style="font-size: 20px;display: block">${top3[sta.index].top2.name}</a></td>
                                <td style="display:table-cell; vertical-align:middle" class="top1_col">${ac_sum[sta.index].top2}</td>
                                <td style="display:table-cell; vertical-align:middle" class="top1_col">${top3[sta.index].top2.ranting}</td>
                                </c:if>
                                <c:if test="${empty top3[sta.index].top2}"><td></td><td></td><td></td></c:if>
                                <c:if test="${not empty top3[sta.index].top3}">
                                <td style="display:table-cell; vertical-align:middle" class="top2_col"><img src="${pageContext.request.contextPath}${top3[sta.index].top3.head_img}" alt="头像" class="img-circle" style="width: 30px;height: 30px"><a href="${pageContext.request.contextPath}/user?id=${top3[sta.index].top3.id}" style="font-size: 20px;display: block">${top3[sta.index].top3.name}</a></td>
                                <td style="display:table-cell; vertical-align:middle" class="top1_col">${ac_sum[sta.index].top3}</td>
                                <td style="display:table-cell; vertical-align:middle" class="top1_col">${top3[sta.index].top3.ranting}</td>
                                </c:if>
                                <c:if test="${empty top3[sta.index].top3}"><td></td><td></td><td></td></c:if>
                            </tr>
                        </c:forEach>
<%--                    </c:if>--%>
                    </tbody>
                </table>
        </div>
    </div>
<div id="footer"></div>
</body>
</html>