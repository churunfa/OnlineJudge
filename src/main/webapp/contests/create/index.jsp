<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="OnlineJudge.domain.Contest" %>
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
    <title>创建比赛</title>
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
    <link rel="stylesheet" href="./css/create.css">
    <script src="js/create.js"></script>

    <link type="text/css" rel="stylesheet" href="../../js/jeDate/test/jeDate-test.css">
    <link type="text/css" rel="stylesheet" href="../../js/jeDate/skin/jedate.css">
    <script type="text/javascript" src="../../js/jeDate/src/jedate.js"></script>
<!--    <script type="text/javascript" src="../../js/jeDate/test/demo.js"></script>-->


</head>
<body style="padding-top: 60px;
    min-width: 800px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);"  hidden>
<%
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    String id = request.getParameter("id");
    if(id == null ) request.setAttribute("id",0);
    else {
        Contest contest = contestsDao.findContestByCid(Integer.parseInt(id));
        if(contest == null){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }
        User_password user = (User_password) request.getSession().getAttribute("User");
        if("normal".equals(user.getPower())){
            if(contest.getMaster() != user.getId()){
                request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
                return;
            }
        }
        request.setAttribute("id",id);
    }

%>
<div id="header"></div>
<div class="container main">
    <form action="">
        <input type="text" class="form-control" placeholder="比赛标题" id="title">
    </form>
    <h4 style="margin: 30px">比赛说明：</h4>
    <textarea class="form-control" rows="6" id="desc"></textarea>
    <div class="row">
        <div class="col-md-3">
            <div class="jeitem" style="margin-top: 20px;margin-left: 100px">
                <div class="jeinpbox"><input type="text" onfocus=this.blur() class="jeinput" id="start" placeholder="请选择比赛开始时间"></div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="jeitem" style="margin-top: 20px;margin-left: 100px">
                <div class="jeinpbox"><input type="text" onfocus=this.blur() class="jeinput" id="end" placeholder="请选择比赛结束时间"></div>
            </div>
        </div>
        <div class="col-md-3">
            <select id="type" class="form-control" style="margin-top: 20px;margin-left: 100px">
                <option value="div3" selected>div3</option>
                <option value="div2">div2</option>
                <option value="div1">div1</option>
                <option value="欢乐赛">欢乐赛</option>
            </select>
        </div>
        <div style="overflow: hidden;">
            <a type="button" id="submit" data-loading-text="提交中..." class="btn btn-default" autocomplete="off">
                <c:if test="${id == 0}">
                    创建
                </c:if>
                <c:if test="${id != 0}">
                    修改
                </c:if>
            </a>
<%--            <a id="submit">--%>
<%--                <c:if test="${id == 0}">--%>
<%--                    创建--%>
<%--                </c:if>--%>
<%--                <c:if test="${id != 0}">--%>
<%--                    修改--%>
<%--                </c:if>--%>
<%--            </a>--%>
        </div>
    </div>
</div>
<div hidden id="path">${pageContext.request.contextPath}</div>
<div id="footer"></div>
</body>
</html>