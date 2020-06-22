<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="OnlineJudge.domain.Contest" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>倒计时</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="https://at.alicdn.com/t/font_1191451_h720mljzrsc.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link href="../../css/bootstrap.min.css" rel="stylesheet">
<script src="../../js/jquery-1.11.0.min.js"></script>
<script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
<script src="js/jweixin-1.4.0.js"></script> 
<script src="js/app.js"></script>
<script src="../../js/bootstrap.js"></script>
<link rel="icon" type="image/png" sizes="144x144" href="../../imgs/logo_blue_144.png"/>


</head>
<body style="padding-top: 60px;
    min-height: 750px;"  hidden>

<%
	String id = request.getParameter("id");
	if(id == null){
		request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
		return;
	}

	ContestsDaoImpl contestsDao = new ContestsDaoImpl();
	Contest contestByCid = contestsDao.findContestByCid(Integer.parseInt(id));

	long st = contestByCid.getStart_time().getTime();
	long now = new Date().getTime();
	if(now >= st){
		response.sendRedirect(request.getContextPath()+"/contests/contests_show/?id="+id);
		return;
	}
	request.setAttribute("contest",contestByCid);
%>
<div hidden id="title">Rand #${contest.id} ${contest.name}</div>
<div id="header"></div>
<div class="key">
	<div class="iocnBox"><i class="iconfont icon-delete"></i></div>
	<div class="empty">清空</div>
	<textarea placeholder="在此发表心情..." rows="1" class="van-field__control"></textarea>
	<div class="buts">发送</div>
</div>
<div class="today">
	<div class="clock">
		<div class="pos SS"></div>
		<div class="pos MM"></div>
		<div class="pos HH"></div>
		<div class="spot"></div>
	</div>
	<!-- <p>北京时间</p> -->
	<!-- <div class="day"></div>         -->
	<div class="time"></div>
	<div class="sydate"></div>
</div>
<div class="Barrage" id="danMu">
	<span></span>
<%--	<span><img src="../../imgs/crf.png" alt="" style="width: 25px">比赛愉快~</span>--%>
<%--	<span>哈喽啊, 少年！！</span><span class="B-span2">比赛愉快！</span><span>Hello, 2020, happy new year</span>--%>
</div>
<div hidden id="path">${pageContext.request.contextPath}</div>
<div class="message">发送弹幕</div>
<div id="footer"></div>
</body>
</html>