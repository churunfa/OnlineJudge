<%@ page import="OnlineJudge.dao.impl.BlogDaoImpl" %>
<%@ page import="OnlineJudge.util.ReadFileData" %>
<%@ page import="java.io.File" %>
<%@ page import="OnlineJudge.dao.impl.ProblemDaoImpl" %>
<%@ page import="OnlineJudge.domain.*" %>
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
    <title>创建题解</title>
    <!-- Bootstrap -->
    <link href="../../../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../../../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../../../js/bootstrap.min.js"></script>
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
    <link rel="stylesheet" href="./css/create.css">
    <script src="./js/create.js"></script>

    <!--Markdown-->
    <link rel="stylesheet" href="../../../js/editor.md-master/css/editormd.css">
    <link rel="stylesheet" href="../../../js/editor.md-master/examples/css/style.css">
<%--    <script src="../../../js/editor.md-master/examples/js/jquery.min.js"></script>--%>
    <script src="../../../js/editor.md-master/editormd.min.js"></script>

</head>
<body style="padding-top: 60px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);
    min-width: 800px"  hidden>
<%
    BlogDaoImpl blogDao = new BlogDaoImpl();
    ProblemDaoImpl problemDao = new ProblemDaoImpl();

    String pids = request.getParameter("pid");
    String ids = request.getParameter("id");
    if(pids == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }
    int pid = Integer.parseInt(pids);

    Problem problem = problemDao.findProblemByPid(pid);

    if(problem == null) {
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    if(!problem.isIs_show()){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    int id = 0;
    if(ids != null) id = Integer.parseInt(ids);

    String title = "";
    String text = "";

    if(id != 0){
        Solution blog = blogDao.findBlogById(id);
        if(blog == null){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }
        User_password user = (User_password) request.getSession().getAttribute("User");
        if(user == null ){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }
        if(blog.getMaster()!=  user.getId() ){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }
        title = blog.getTitle();

        String url = blog.getPath();
        String realPath = application.getRealPath(url);
        System.out.println(realPath);
        File file = new File(realPath);
        text = ReadFileData.txt2String(file,false);
    }

    request.setAttribute("title",title);
    request.setAttribute("text",text);
    request.setAttribute("pro",problem);
    request.setAttribute("id",id);


%>
<div id="header"></div>
<div class="container">
    <h1 style="text-align: center;margin-top: 20px">${pro.title}</h1>
    <div class="row" style="padding-top: 50px">
        <h2 class="col-md-3" style="text-align: right">标题:</h2>
        <textarea rows="1" id="title" class="form-control autofit col-md-9" maxlength="2000" style="background-color: rgb(248, 248, 248); border-radius: 5px; overflow: hidden; resize: none; font-family: monospace; min-height: 35px; height: 0px; width: 600px;">${title}</textarea>
    </div>
    <div id="test-editormd" style="margin-top:50px">
        <textarea style="display:none;" id="user_input">${text}</textarea>
    </div>
    <div id="submit">
        <a type="button" id="sum" data-loading-text="保存中..." class="btn btn-primary" autocomplete="off">
            保存
        </a>
        <a type="button" id="ans" data-loading-text="提交中..." class="btn btn-primary" autocomplete="off">
            提交
        </a>
<%--        <a class="" id="sum">保存</a>--%>
<%--        <a class="" id="ans">提交</a>--%>
    </div>
    <div hidden id="path">${pageContext.request.contextPath}</div>
    <div hidden id="pid">${pro.pid}</div>
    <div hidden id="id">${id}</div>
</div>
<div id="footer"></div>
</body>
</html>