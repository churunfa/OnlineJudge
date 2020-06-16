<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="java.util.List" %>
<%@ page import="OnlineJudge.service.impl.ContestsServiceImpl" %>
<%@ page import="OnlineJudge.domain.User" %>
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
    <title>报名名单</title>
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
    <link rel="stylesheet" href="css/signlist.css">
    <script src="js/signlist.js"></script>
</head>
<body style="padding-top: 60px;
    min-width: 800px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);"  hidden>
<%
    String id = request.getParameter("id");
    if(id == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    ContestsServiceImpl contestsService = new ContestsServiceImpl();

    if(contestsService == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    List<User> users = contestsService.findContestSign(Integer.parseInt(id));

    request.setAttribute("users",users);

%>
<div id="header"></div>
<div class="container main">
    <table class="table table-striped">
        <thead>
        <tr>
            <th id="th1">#</th>
            <th id="th2">用户</th>
           <th id="th6">Ranting评分<span style=""></span></th>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <c:forEach items="${users}" var="user" varStatus="sta">
            <tr>
                <td style="display:table-cell; vertical-align:middle">${sta.index+1}</td>
                <td style="display:table-cell; vertical-align:middle"><img src="${pageContext.request.contextPath}${user.head_img}" alt="头像" class="img-circle" style="width: 30px"><a href="${pageContext.request.contextPath}/user?id=${user.id}" style="font-size: 20px;display: block">${user.name}</a></td>
                <td style="display:table-cell; vertical-align:middle">${user.ranting}</td>
            </tr>
        </c:forEach>
<%--        <tr>--%>
<%--            <td style="display:table-cell; vertical-align:middle">1</td>--%>
<%--            <td style="display:table-cell; vertical-align:middle"><img src="../../imgs/crf.png" alt="头像" class="img-circle" style="width: 30px"><a href="#" style="font-size: 20px;display: block">褚润发</a></td>--%>
<%--            <td style="display:table-cell; vertical-align:middle">1500</td>--%>
<%--        </tr>--%>
        </tbody>
    </table>
<%--    <nav style="text-align: center" aria-label="Page navigation">--%>
<%--        <ul class="pagination">--%>
<%--            <li>--%>
<%--                <a href="#" aria-label="Previous">--%>
<%--                    <span aria-hidden="true">&laquo;</span>--%>
<%--                </a>--%>
<%--            </li>--%>
<%--            <li><a href="#">1</a></li>--%>
<%--            <li><a href="#">2</a></li>--%>
<%--            <li><a href="#">3</a></li>--%>
<%--            <li><a href="#">4</a></li>--%>
<%--            <li><a href="#">5</a></li>--%>
<%--            <li>--%>
<%--                <a href="#" aria-label="Next">--%>
<%--                    <span aria-hidden="true">&raquo;</span>--%>
<%--                </a>--%>
<%--            </li>--%>
<%--            <li>--%>
<%--                <form class="navbar-form navbar-left" id="change_page">--%>
<%--                    <div class="form-group">--%>
<%--                        <input type="text" class="form-control" id="page_key" placeholder="页码">--%>
<%--                        <button class="btn btn-default glyphicon glyphicon-share-alt" id="page_btn"></button>--%>
<%--                    </div>--%>
<%--                </form>--%>
<%--            </li>--%>
<%--        </ul>--%>
<%--    </nav>--%>
</div>
<div id="footer"></div>
</body>
</html>