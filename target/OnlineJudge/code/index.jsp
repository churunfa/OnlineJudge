<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="OnlineJudge.dao.impl.ProblemDaoImpl" %>
<%@ page import="org.springframework.web.servlet.view.InternalResourceViewResolver" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@ page import="java.util.Date" %>
<%@ page import="OnlineJudge.domain.User_password" %>
<%@ page import="OnlineJudge.dao.impl.StatusDaoImpl" %>
<%@ page import="OnlineJudge.dao.impl.QueueDaoImpl" %>
<%@ page import="OnlineJudge.domain.Standard_code" %><%--
  Created by IntelliJ IDEA.
  User: crf
  Date: 2020/6/14
  Time: 下午1:33
  To change this template use File | Settings | File Templates.
--%>

<%
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    QueueDaoImpl queueDao = new QueueDaoImpl();


    String solution_id = request.getParameter("id");
    String pid = request.getParameter("pid");
    User_password user = (User_password) request.getSession().getAttribute("User");

    if(pid == null && solution_id == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    String code="";
    String language = "";
    boolean flag = false;
    if(pid != null){
        flag = true;
        Problem problem = problemDao.findProblemByPid(Integer.parseInt(pid));
        if(problem == null){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }


        if(!problem.isIs_show()){
            if(user == null){
                request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
                return;
            }
            if(!"root".equals(user.getPower()) && user.getId() != problem.getMaster()){
                request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
                return;
            }
        }
        request.setAttribute("pro",problem);
        Standard_code standard_code = problemDao.getStandard_code(Integer.parseInt(pid));
        if(standard_code == null) code = "";
        else{
            code = standard_code.getCode();
            language = standard_code.getLanguage();
        }
    }else{
        int pid1 = queueDao.findPid(Integer.parseInt(solution_id));
        if(pid1 == 0){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }
        Problem problemByPid = problemDao.findProblemByPid(pid1);
        if(problemByPid == null){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }

        if(!problemByPid.isIs_show()){
            if(user == null){
                request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
                return;
            }
            if(!"root".equals(user.getPower()) && user.getId() != problemByPid.getMaster()){
                request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
                return;
            }
        }

        code = queueDao.findSolutionCode(Integer.parseInt(solution_id));
        if(code == null) code = "";
        request.setAttribute("pro",problemByPid);
    }
    request.setAttribute("code",code);
    request.setAttribute("language",language);
    request.setAttribute("flag",flag);
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" style="min-height: 100%;min-width: 800px">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${pro.title}</title>
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="./js/code.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="icon" type="image/png" sizes="144x144" href="../imgs/logo_blue_144.png"/>
    <script src="../js/showdown.min.js"></script>
    <script src="https://cdn.bootcss.com/ace/1.4.6/ace.js"></script>
    <script src="https://cdn.bootcss.com/ace/1.4.6/ext-beautify.js"></script>
    <script src="https://cdn.bootcss.com/ace/1.4.6/ext-language_tools.js"></script>
    <script src="https://cdn.bootcss.com/ace/1.4.6/mode-javascript.js"></script>
    <script src="https://cdn.bootcss.com/ace/1.4.6/theme-xcode.js"></script>
    <script src="http://cdn.bootcss.com/mathjax/2.7.0/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
    <script type="text/x-mathjax-config">
    MathJax.Hub.Config({
        tex2jax: {inlineMath: [['$', '$']]},
        messageStyle: "none"
    });
    </script>
</head>
<body style="padding-top: 60px;
    min-height: 100%;
    min-width: 800px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);"  hidden>
    <div id="header"></div>
    <div class="container" style="min-height: 500px;padding-top: 30px">
        <c:if test="${flag}">
            <p>${language}标程：</p>
        </c:if>
        <div id="code"></div>
    </div>
    <textarea hidden id="get_code">${code}</textarea>
<div id="footer"></div>
</body>
</html>