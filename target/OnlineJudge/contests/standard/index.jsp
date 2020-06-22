<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="OnlineJudge.dao.impl.ProblemDaoImpl" %>
<%@ page import="org.springframework.web.servlet.view.InternalResourceViewResolver" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@ page import="java.util.Date" %>
<%@ page import="OnlineJudge.domain.User_password" %>
<%@ page import="OnlineJudge.dao.impl.StatusDaoImpl" %>
<%@ page import="OnlineJudge.dao.impl.QueueDaoImpl" %>
<%@ page import="OnlineJudge.domain.Standard_code" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: crf
  Date: 2020/6/14
  Time: 下午1:33
  To change this template use File | Settings | File Templates.
--%>

<%
    User_password user = (User_password) request.getSession().getAttribute("User");
    if(user == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }
    String pid = request.getParameter("pid");
    if(pid == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    Problem problem = problemDao.findProblemByPid(Integer.parseInt(pid));
    if(problem == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    if(!"root".equals(user.getPower())&&user.getId()!=problem.getMaster()){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }
    String standard_code = "";
    String nowLanguage = "";
    Standard_code standard_language = problemDao.getStandard_code(problem.getPid());

    if(standard_language == null) standard_code = "";
    else {
        standard_code = standard_language.getCode();
        nowLanguage = standard_language.getLanguage();
    }

    List<String> languages = problemDao.findLanguages(problem.getPid());

    boolean flag=false;
    if(standard_code != null && standard_code.length()!=0) flag = true;
    request.setAttribute("flag",flag);
    request.setAttribute("standard_code",standard_code);
    request.setAttribute("languages",languages);
    request.setAttribute("nowLanguage",nowLanguage);
    request.setAttribute("pro",problem);

%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" style="min-height: 100%;min-width: 800px">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>添加/修改标程</title>
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="js/standard.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="icon" type="image/png" sizes="144x144" href="../../imgs/logo_blue_144.png"/>
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
    <div hidden id="pid">${pro.pid}</div>
    <div id="header"></div>
    <p style="font-size: 30px;text-align: center;margin-top: 20px">Round #${pro.contest_id} ${pro.type}.${pro.title}</p>
    <div class="container" style="min-height: 500px;padding-top: 30px">
        <select class="form-control" id="languages">
            <c:forEach items="${languages}" var="language" varStatus="sta">
                <c:if test="${flag}">
                    <c:if test="${nowLanguage == language}">
                        <option selected>${language}</option>
                    </c:if>
                    <c:if test="${nowLanguage != language}">
                        <option>${language}</option>
                    </c:if>
                </c:if>
                <c:if test="${!flag}">
                    <c:if test="${sta.index == 0}">
                        <option selected>${language}</option>
                    </c:if>
                    <c:if test="${sta.index != 0}">
                        <option>${language}</option>
                    </c:if>
                </c:if>
            </c:forEach>
        </select>
        <textarea id="code" class="form-control" rows="30">${standard_code}</textarea>
    </div>
    <button type="button" style="margin-left: 50%;margin-top: 10px" id="sub" data-loading-text="正在提交..." class="btn btn-default" autocomplete="off">
        提交
    </button>
<div id="footer"></div>
</body>
</html>