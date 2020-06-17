<%@ page import="OnlineJudge.service.RankService" %>
<%@ page import="OnlineJudge.service.impl.RankServiceImpl" %>
<%@ page import="OnlineJudge.domain.RankInfo" %>
<%@ page import="OnlineJudge.domain.PageBean" %>
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
    <title>排名</title>
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
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
    <link rel="stylesheet" href="./css/rank.css">
    <script src="js/rank.js"></script>
</head>
<body style="padding-top: 60px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);
    min-width: 800px"  hidden>
<%
    int type=4,pg=1,size=20;

    RankServiceImpl rankService = new RankServiceImpl();
    String types = request.getParameter("type");
    String pgs = request.getParameter("pg");
    String sizes = request.getParameter("size");
    if(types != null) type = Integer.parseInt(types);
    if(pgs != null) pg = Integer.parseInt(pgs);
    if(sizes != null) size = Integer.parseInt(sizes);

    PageBean<RankInfo> ranks = rankService.findRanks(type, pg, size);

    request.setAttribute("size",ranks.getPageSize());
    request.setAttribute("pgs",ranks.getPageSize());
    request.setAttribute("type",type);
    request.setAttribute("maxn",ranks.getTotalPage());
    request.setAttribute("info",ranks.getList());

    int st=Math.max(ranks.getCurrentPage()-2,1);
    int ed = st;
    for(int i=0;i<5;i++){
        if(st+i>ranks.getTotalPage()) break;
        ed=st+i;
    }

    while(ed-st+1<4){
        if(st==1) break;
        st--;
    }
    request.setAttribute("st",st);
    request.setAttribute("ed",ed);


%>
<div id="header"></div>
<div class="container main">
    <table class="table table-striped">
        <thead>
        <tr>
            <th id="th1">#</th>
            <th id="th2">用户</th>
            <th id="th3">总过题数<span style="display:none" class="glyphicon glyphicon-triangle-bottom sp"></span></th>
            <th id="th4">比赛场数<span style="display:none" class="glyphicon glyphicon-triangle-bottom sp"></span></th>
            <th id="th5">出题场数<span style="display:none" class="glyphicon glyphicon-triangle-bottom sp"></span></th>
            <th id="th6">Ranting评分<span style="display: none" class="glyphicon glyphicon-triangle-bottom sp"></span></th>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <c:forEach items="${info}" var="data">
            <tr>
                <td style="display:table-cell; vertical-align:middle">${data.rank}</td>
                <td style="display:table-cell; vertical-align:middle"><img src="${pageContext.request.contextPath}${data.user.head_img}" alt="头像" class="img-circle" style="width: 30px;height: 30px"><a href="${pageContext.request.contextPath}/user?id=${data.user.id}" style="font-size: 20px;display: block">${data.user.name}</a></td>
                <td style="display:table-cell; vertical-align:middle">${data.ac_sum}</td>
                <td style="display:table-cell; vertical-align:middle">${data.sign_sum}</td>
                <td style="display:table-cell; vertical-align:middle">${data.con_sum}</td>
                <td style="display:table-cell; vertical-align:middle">${data.ranting}</td>
            </tr>
        </c:forEach>
<%--        <tr>--%>
<%--            <td style="display:table-cell; vertical-align:middle">1</td>--%>
<%--            <td style="display:table-cell; vertical-align:middle"><img src="../imgs/crf.png" alt="头像" class="img-circle" style="width: 30px"><a href="#" style="font-size: 20px;display: block">褚润发</a></td>--%>
<%--            <td style="display:table-cell; vertical-align:middle">1</td>--%>
<%--            <td style="display:table-cell; vertical-align:middle">1</td>--%>
<%--            <td style="display:table-cell; vertical-align:middle">1</td>--%>
<%--            <td style="display:table-cell; vertical-align:middle">1500</td>--%>
<%--        </tr>--%>
        </tbody>
    </table>
    <nav style="text-align: center" aria-label="Page navigation">
        <ul class="pagination">
            <li>
                <a href="${pageContext.request.contextPath}/rank?size=${size}&type=${type}" aria-label="first">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <c:forEach begin="${st}" end="${ed}" var="i">
                <li><a href="${pageContext.request.contextPath}/rank?pg=${i}&size=${size}&type=${type}">${i}</a></li>
            </c:forEach>

            <%--                    <li><a href="#">2</a></li>--%>
            <%--                    <li><a href="#">3</a></li>--%>
            <%--                    <li><a href="#">4</a></li>--%>
            <%--                    <li><a href="#">5</a></li>--%>
            <li>
                <a href="${pageContext.request.contextPath}/rank?pg=${maxn}&size=${size}&type=${type}" aria-label="end">
                    <span aria-hidden="true">&raquo;</span>
                </a>
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
<div id="footer"></div>
<div id="maxPg" hidden>${maxn}</div>
<div id="type" hidden>${type}</div>
</body>
</html>