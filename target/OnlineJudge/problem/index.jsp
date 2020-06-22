<%@ page import="OnlineJudge.dao.impl.UserDaoImpl" %>
<%@ page import="OnlineJudge.domain.Tag" %>
<%@ page import="java.util.List" %>
<%@ page import="OnlineJudge.service.impl.UserServiceImpl" %>
<%@ page import="OnlineJudge.domain.PageBean" %>
<%@ page import="OnlineJudge.domain.ProblemInfo" %>
<%@ page import="OnlineJudge.dao.StatusDao" %>
<%@ page import="OnlineJudge.dao.impl.StatusDaoImpl" %>
<%@ page import="OnlineJudge.domain.User_password" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en"  style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>题库</title>
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
    <link rel="stylesheet" href="./css/problem.css">
    <script src="./js/problem.js"></script>

</head>
<body hidden  style="min-width: 800px;">

<%

    UserServiceImpl userService = new UserServiceImpl();
    UserDaoImpl userDao = new UserDaoImpl();


    //    pg:当前页码
    //    size:每页显示条数
    //    tag:tag id

    int pg=1,size = 20,tag = 0 ;
    String pgs = (String) request.getParameter("pg");
    if(pgs != null) pg = Integer.parseInt(pgs);

    String sizes = (String) request.getParameter("size");
    if(sizes != null) size = Integer.parseInt(sizes);

    String tags = (String) request.getParameter("tag");
    if(tags != null) tag = Integer.parseInt(tags);


    List<Tag> allTag = userDao.findAllTag();
    request.setAttribute("tags",allTag);
    PageBean<ProblemInfo> pageBeanByPage = new PageBean<ProblemInfo>();
    if(tag == 0) pageBeanByPage = userService.findPageBeanByPage(pg, size);
    else pageBeanByPage = userService.findPageBeanByTag(pg,size,tag);

    List<ProblemInfo> list = pageBeanByPage.getList();


    StatusDaoImpl statusDao = new StatusDaoImpl();
    User_password user = (User_password) request.getSession().getAttribute("User");

    for(int i=0;i<list.size();i++){
        ProblemInfo pro = list.get(i);
        String title = list.get(i).getProblem().getTitle();
        if(user == null){
            pro.setPos(0);
            list.set(i,pro);
            continue;
        }
        int flag = statusDao.checkSub(list.get(i).getProblem().getPid(),user.getId());
        if(flag == 0){
            pro.setPos(0);
            list.set(i,pro);
            continue;
        }
        int ac = statusDao.checkAcOrNoByPidAndUid(list.get(i).getProblem().getPid(),user.getId());
        if(ac == 0){
            pro.setPos(1);
            list.set(i,pro);
            continue;
        }
        pro.setPos(2);
        list.set(i,pro);
    }

    request.setAttribute("size",pageBeanByPage.getPageSize());
    request.setAttribute("nowPage",pageBeanByPage.getCurrentPage());
    request.setAttribute("max",pageBeanByPage.getTotalPage());
    request.setAttribute("pros",list);
    request.setAttribute("tag",tag);

    int st=Math.max(pageBeanByPage.getCurrentPage()-2,1);
    int ed = st;
    for(int i=0;i<5;i++){
        if(st+i>pageBeanByPage.getTotalPage()) break;
        ed=st+i;
    }

    while(ed-st+1<4){
        if(st==1) break;
        st--;
    }

    request.setAttribute("st",st);
    request.setAttribute("ed",ed);



%>
<div id="header">
</div>
<div class="main">
    <div class="row">
        <div class="col-md-9" >
            <div class="panel panel-default" id="left">
                <table class="table table-hover table-hover" id="pro">
                    <thead>
<!--                        glyphicon glyphicon-menu-up-->
                        <tr>
                            <th class="th1">#
                            </th>
                            <th class="th2">标题</th>
                            <th class="th3">标签</th>
                            <th class="th4">通过率
                            </th>
                            <th class="th5">评分
                            </th>
                            <th class="th6">来源</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${pros}" var="pro">
                            <tr>
                                <td style="display:table-cell; vertical-align:middle;
                                    <c:if test="${pro.pos == 1}">
                                            background-color: #FFCCCC;
                                    </c:if>
                                    <c:if test="${pro.pos == 2}">
                                            background-color: #CCFFFF;
                                    </c:if>
                                ">${pro.problem.contest_id}-${pro.problem.type}</td>
                                <td style="display:table-cell; vertical-align:middle">
                                    <a href="./content?pid=${pro.problem.pid}">${pro.problem.title}</a>
                                </td>
                                <td style="display:table-cell; vertical-align:middle">
                                    <div class="left-tag" style="overflow:hidden;">
                                        <c:forEach items="${pro.tags}" var="tag">
                                            <a href="${pageContext.request.contextPath}/problem?tag=${tag.id}">${tag.name}</a>
                                        </c:forEach>
                                    </div>
                                </td>
                                <td style="display:table-cell; vertical-align:middle"><fmt:formatNumber type="number" value="${pro.rate}" maxFractionDigits="2"/>%(${pro.ac}/${pro.sum})</td>
                                <td style="display:table-cell; vertical-align:middle">${pro.problem.ranting}</td>
                                <td style="display:table-cell; vertical-align:middle">${pro.problem.source}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <nav style="text-align: center" aria-label="Page navigation">
                <ul class="pagination">
                    <li>
                        <a href="${pageContext.request.contextPath}/problem?size=${size}&tag=${tag}" aria-label="first">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <c:forEach begin="${st}" end="${ed}" var="i">
                        <li><a href="${pageContext.request.contextPath}/problem?pg=${i}&size=${size}&tag=${tag}">${i}</a></li>
                    </c:forEach>

<%--                    <li><a href="#">2</a></li>--%>
<%--                    <li><a href="#">3</a></li>--%>
<%--                    <li><a href="#">4</a></li>--%>
<%--                    <li><a href="#">5</a></li>--%>
                    <li>
                        <a href="${pageContext.request.contextPath}/problem?pg=${max}&size=${size}&tag=${tag}" aria-label="end">
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
        <div class="col-md-3">
            <form class="navbar-form navbar-left" id="search_form">
                <div style="padding: 10px;">查找题目</div>
                <div class="form-group">
                    <input type="text" class="form-control" id="search_key" disabled="disabled" placeholder="关键字">
                    <button class="btn btn-default glyphicon glyphicon-search" disabled="disabled" id="search_btn"></button>
                </div>
            </form>
            <div id="tag">
                <div style="padding-top: 15px;padding-left: 15px">标签</div>
                <div style="padding: 15px; overflow:hidden;">
                    <c:forEach items="${tags}" var="tag">
                        <a href="${pageContext.request.contextPath}/problem?tag=${tag.id}">${tag.name}</a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
<div hidden id="maxPg">${max}</div>
<div id="footer"></div>
</body>
</html>