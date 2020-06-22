<%@ page import="OnlineJudge.service.impl.StatusServiceImpl" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="OnlineJudge.domain.*" %>
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
    <title>提交记录</title>
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="./js/status.js"></script>
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
    <link rel="stylesheet" href="./css/status.css">
</head>
<body style="padding-top: 60px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);
    min-width: 800px;
"  hidden>
<%
    //pg:当前页码
    //size:每页显示条数
    //uname:作者姓名
    //title:题目
    //type:语言
    //sta:状态

    StatusServiceImpl statusService = new StatusServiceImpl();

    int pg=1,size=20,pid = 0;
    String uname=null,title=null,type=null,sta=null;

    String realPg = request.getParameter("pg");
    String realSize = request.getParameter("size");
    String pid1 = request.getParameter("pid");
    if(realPg != null) pg = Integer.parseInt(realPg);
    if(realSize != null) size = Integer.parseInt(realSize);
    if(pid1 != null) pid=Integer.parseInt(pid1);
    uname = request.getParameter("uname");
    title = request.getParameter("title");
    type = request.getParameter("type");
    sta = request.getParameter("sta");

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPg(pg);
    pageInfo.setSize(size);
    pageInfo.setType(type);
    pageInfo.setSta(sta);
    pageInfo.setTitle(title);
    pageInfo.setUname(uname);
    pageInfo.setPid(pid);

    User_password sign_user = (User_password) request.getSession().getAttribute("User");
    if(sign_user == null) pageInfo.setSign_uid(0);
    else pageInfo.setSign_uid(sign_user.getId());

    PageBean<StatusInfo> status = statusService.findStatus(pageInfo);

    request.setAttribute("pg",pg);
    request.setAttribute("size",size);
    request.setAttribute("type",type);
    request.setAttribute("sta",sta);
    request.setAttribute("title",title);
    request.setAttribute("uname",uname);
    request.setAttribute("maxn",status.getTotalPage());
    request.setAttribute("pid",pid);

    request.setAttribute("data",status.getList());

    List<Boolean> isShows = new ArrayList<Boolean>();

    User_password user = (User_password) request.getSession().getAttribute("User");

    List<StatusInfo> list = status.getList();
    for(int i=0;i<list.size();i++){
        Problem problem = list.get(i).getProblem();
        if(problem.isIs_show()){
            isShows.add(true);
            continue;
        }
        if(user != null && "root".equals(user.getPower())){
            isShows.add(true);
            continue;
        }
        if(user != null &&  problem.getMaster() == user.getId()){
            isShows.add(true);
            continue;
        }
        isShows.add(false);
    }

    request.setAttribute("isShows",isShows);


    int st=Math.max(status.getCurrentPage()-2,1);
    int ed = st;
    for(int i=0;i<5;i++){
        if(st+i>status.getTotalPage()) break;
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
    <div class="main container">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>#</th>
                    <th>提交时间</th>
                    <th>
                        <form class="navbar-form navbar-left" id="statue-user-form">
                            <div class="form-group">
                                <input type="text" class="form-control" id="statue-user" disabled="disabled" placeholder="作者">
                            </div>
                        </form>
                    </th>
                    <th>
                        <form class="navbar-form navbar-left" id="statue-pro-form">
                            <div class="form-group">
                                <input type="text" class="form-control" id="statue-pro" disabled="disabled" placeholder="题目">
                            </div>
                        </form>
                    </th>
                    <th>
                        <form class="navbar-form navbar-left" id="statue-lau-form">
                            <select class="form-control" disabled="disabled" id="statue-lau">
                                <option value="" >全部</option>
                                <option>C</option>
                                <option>C++</option>
                                <option>Python</option>
                                <option>JAVA</option>
                            </select>
                        </form>
                    </th>
                    <th>
                        <form class="navbar-form navbar-left" id="statue-sta-form">
                            <div class="form-group">
                                <select class="form-control" id="statue-sta"  disabled="disabled" >
                                    <option value="">全部</option>
                                    <option>答案正确</option>
                                    <option>答案错误</option>
                                    <option>运行超时</option>
                                    <option>运行错误</option>
                                    <option>编译错误</option>
                                    <option>系统错误</option>
                                    <option>格式错误</option>
                                    <option>内存超限</option>
                                    <option>等待测试</option>
                                </select>
                            </div>
                        </form>
                    </th>
                    <th>时间</th>
                    <th>空间</th>
                    <th>代码长度</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${data}" var="dat" varStatus="sta">
                    <tr>
                        <td style="display:table-cell; vertical-align:middle">
                            <c:if test="${isShows[sta.index]}">
                                <a href="${pageContext.request.contextPath}/code?id=${dat.status.id}">${dat.status.id}</a>
                            </c:if>
                            <c:if test="${isShows[sta.index]==false}">
                                <a>${dat.status.id}</a>
                            </c:if>
                        </td>
                        <td style="display:table-cell; vertical-align:middle">
                            <span><fmt:formatDate value="${dat.status.sub_time}" pattern="yyyy-MM-dd"/></span><br>
                            <spanp style="font-size: 10px"><fmt:formatDate value="${dat.status.sub_time}" pattern="HH时mm分"/></spanp>
                        </td>
                        <td style="display:table-cell; vertical-align:middle"><a href="${pageContext.request.contextPath}/user?id=${dat.user.id}">${dat.user.name}</a></td>
<%--                        <td style="display:table-cell; vertical-align:middle"><a href="${pageContext.request.contextPath}/problem/content?pid=${dat.problem.pid}">${dat.problem.title}</a></td>--%>
                        <c:if test="${dat.problem.is_show == false}">
                            <td style="display:table-cell; vertical-align:middle"><a href="${pageContext.request.contextPath}/contests/problem_show/?id=${dat.problem.pid}">${dat.problem.title}</a></td>
                        </c:if>
                        <c:if test="${dat.problem.is_show}">
                            <td style="display:table-cell; vertical-align:middle"><a href="${pageContext.request.contextPath}/problem/content?pid=${dat.problem.pid}">${dat.problem.title}</a></td>
                        </c:if>
                        <td style="display:table-cell; vertical-align:middle">${dat.status.code_type}</td>
                        <td style="display:table-cell; vertical-align:middle"><div type="button" class="btn btn btn-info
                         <c:if test="${dat.status.status=='答案正确'}">alert-success</c:if>
                         <c:if test="${dat.status.status=='答案错误'}">alert-danger</c:if>
                         <c:if test="${dat.status.status=='运行超时'}">alert-danger</c:if>
                         <c:if test="${dat.status.status=='运行错误'}">alert-danger</c:if>
                         <c:if test="${dat.status.status=='编译错误'}">alert-warning</c:if>
                         <c:if test="${dat.status.status=='系统错误'}">alert-danger</c:if>
                         <c:if test="${dat.status.status=='格式错误'}">alert-info</c:if>
                         <c:if test="${dat.status.status=='内存超限'}">alert-danger</c:if>
                         <c:if test="${dat.status.status=='等待测试'}">alert-info</c:if>

                        " style="font-size: 13px;padding: 2px;">${dat.status.status}</div></td>
                        <td style="display:table-cell; vertical-align:middle">${dat.status.run_time}</td>
                        <td style="display:table-cell; vertical-align:middle">${dat.status.memory}</td>
                        <td style="display:table-cell; vertical-align:middle">${dat.status.length}</td>
                    </tr>
                </c:forEach>
<%--                <tr>--%>
<%--                <td style="display:table-cell; vertical-align:middle"><a href="#">${}</a></td>--%>
<%--                <td style="display:table-cell; vertical-align:middle">--%>
<%--                    <span>2020-5-7</span><br>--%>
<%--                    <spanp style="font-size: 10px">10点30分</spanp>--%>
<%--                </td>--%>
<%--                <td style="display:table-cell; vertical-align:middle"><a href="#">crf</a></td>--%>
<%--                <td style="display:table-cell; vertical-align:middle"><a href="#">A+B问题</a></td>--%>
<%--                <td style="display:table-cell; vertical-align:middle">c++</td>--%>
<%--                <td style="display:table-cell; vertical-align:middle"><div type="button" class="btn btn btn-info" style="font-size: 13px;padding: 2px;">等待测试</div></td>--%>
<%--                <td style="display:table-cell; vertical-align:middle">500ms</td>--%>
<%--                <td style="display:table-cell; vertical-align:middle">500k</td>--%>
<%--                <td style="display:table-cell; vertical-align:middle">1100</td>--%>
<%--            </tr>--%>
            </tbody>
        </table>
        <nav style="text-align: center" aria-label="Page navigation">
            <ul class="pagination">
                <li>
                    <a href="${pageContext.request.contextPath}/status?pg=1&size=${size}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>

                <c:forEach begin="${st}" end="${ed}" var="i">
                    <li
                       <c:if test="${pg == i}"> class="active"</c:if>
                    ><a href="${pageContext.request.contextPath}/status?pg=${i}&size=${size}">${i}</a></li>
                </c:forEach>
                <li>
                    <a href="${pageContext.request.contextPath}/status?pg=${maxn}&size=${size}" aria-label="Next">
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
<div id="footer"></div>
<div id="maxPg" hidden>${maxn}</div>
</body>
</html>