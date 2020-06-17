<%@ page import="OnlineJudge.dao.impl.BlogDaoImpl" %>
<%@ page import="OnlineJudge.domain.Solution" %>
<%@ page import="java.util.List" %>
<%@ page import="OnlineJudge.domain.User" %>
<%@ page import="OnlineJudge.dao.impl.ProblemDaoImpl" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@ page import="OnlineJudge.domain.User_password" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    BlogDaoImpl blogDao = new BlogDaoImpl();

    String id = request.getParameter("id");
    if(id == null ){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }
    Solution blog = blogDao.findBlogById(Integer.parseInt(id));
    request.setAttribute("id",id);
    request.setAttribute("info",blog);

    User_password user = (User_password) request.getSession().getAttribute("User");

    int love = blogDao.findLove(user.getId(), Integer.parseInt(id));

    request.setAttribute("love",love);

%>

<!DOCTYPE html>
<html lang="en" style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${info.title}</title>
    <!-- Bootstrap -->
    <link href="../../../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../../../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../../../js/bootstrap.min.js"></script>
    <script src="../../../js/showdown.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>

    <![endif]-->
    <link rel="icon" type="image/png" sizes="144x144" href="../../../imgs/logo_blue_144.png"/>
    <script src="http://cdn.bootcss.com/mathjax/2.7.0/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
    <script type="text/x-mathjax-config">
    MathJax.Hub.Config({
        tex2jax: {inlineMath: [['$', '$']]},
        messageStyle: "none"
    });
    </script>
    <link rel="stylesheet" href="./css/desc.css">
    <script src="js/desc.js"></script>

</head>
<body style="padding-top: 60px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);
    min-width: 800px"  hidden>

<div id="header"></div>
<div class="main container">
    <div class="row">
        <div class="col-md-1">
            <c:if test="${love == 0}">
                <div class="heart"><span class="glyphicon glyphicon-heart-empty hear"></span> <span class="love_sum">${info.love}</span></div>
            </c:if>
            <c:if test="${love==1}">
                <div class="heart"><span class="glyphicon glyphicon-heart hear"></span> <span class="love_sum">${info.love}</span></div>
            </c:if>
        </div>
        <div class="col-md-11 info">
            <span style="font-size: 40px">${info.title}</span>
            <a href="${pageContext.request.contextPath}/problem/content?pid=${info.pid}" class="btn btn-success" style="font-size: 15px;background-color: #5bc0de;padding: 3px;margin-left: 20px">查看原题</a>
            <div id="sol-desc"></div>
            <div style=" position:relative  ;overflow: hidden">
                <p>讨论区：</p>
                <textarea readonly rows="4" id="comment" class="form-control autofit col-md-9" maxlength="2000";></textarea>
                <a href="javascript:void(0)" class="submit">提交</a>
            </div>
            <div hidden class="mes">
                <div class="panel-heading row comment-css">
                    <div class="row">
                        <div class="col-md-1">
                            <a href="#">
                                <img src="../../../imgs/crf.png" alt="crf" class="img-circle user-img" style="width: 30px;height: 30px">
                            </a>
                            <a href="#" style="font-size: 13px;">回复</a>
                            <a href="#" hidden style="font-size: 13px;">删除</a>
                        </div>
                        <div class="col-md-11" style="text-align: left">
                            <p>
                                水题，~~~~啊啊啊啊啊啊hhhhhhhhhhhhhh
                                a爱就爱放假啊飓风埃夫你 骄傲i叫ioaj哎欧安尼
                                啊你哦分奶粉呢奥就
                                闹我i及哦
                            </p>
                            <div class="row">
                                <div class="col-md-1">
                                    <a href="#">
                                        <img src="../../../imgs/crf.png" alt="crf" class="img-circle user-img" style="width: 30px">
                                    </a>
                                    <a href="#" style="font-size: 13px;">删除</a>
                                </div>
                                <div class="col-md-11" style="text-align: left">
                                    <p>
                                        水题，~~~~啊啊啊啊啊啊hhhhhhhhhhhhhh
                                        a爱就爱放假啊飓风埃夫你 骄傲i叫ioaj哎欧安尼
                                        啊你哦分奶粉呢奥就
                                        闹我i及哦
                                    </p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-1">
                                    <a href="#">
                                        <img src="../../../imgs/crf.png" alt="crf" class="img-circle user-img" style="width: 30px">
                                    </a>
                                    <a href="#" style="font-size: 13px;">删除</a>
                                </div>
                                <div class="col-md-11" style="text-align: left">
                                    <p>
                                        水题，~~~~啊啊啊啊啊啊hhhhhhhhhhhhhh
                                        a爱就爱放假啊飓风埃夫你 骄傲i叫ioaj哎欧安尼
                                        啊你哦分奶粉呢奥就
                                        闹我i及哦
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div hidden>
                        <textarea rows="4" class="reply form-control autofit col-md-9" maxlength="2000";></textarea>
                        <a class="submit">提交</a>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<div hidden id="id">${id}</div>
<div hidden id="path">${pageContext.request.contextPath}</div>
<div id="footer"></div>
</body>
</html>