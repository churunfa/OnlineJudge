<%@ page import="OnlineJudge.dao.impl.ProblemDaoImpl" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@ page import="java.util.List" %>
<%@ page import="OnlineJudge.dao.impl.StatusDaoImpl" %>
<%@ page import="OnlineJudge.dao.impl.UserDaoImpl" %>
<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="OnlineJudge.domain.User_password" %>
<%@ page import="OnlineJudge.util.ReadFileData" %>
<%@ page import="java.io.File" %>
<%@ page import="OnlineJudge.domain.codeInfo" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String pid = request.getParameter("pid");
    if(pid == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }



    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    UserDaoImpl userDao = new UserDaoImpl();
    StatusDaoImpl statusDao = new StatusDaoImpl();
    Problem problem = problemDao.findProblemByPid(Integer.parseInt(pid));
    if(problem == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    if(!problem.isIs_show()){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    int sum = userDao.findAllStatusCountByPid(Integer.parseInt(pid));
    int ac = userDao.findStatusByPidCount("答案正确", Integer.parseInt(pid));
    double r = 0;
    if(sum != 0 ) r = 100.0*ac/sum;

    request.setAttribute("ac",ac);
    request.setAttribute("sum",sum);
    request.setAttribute("r",r);

    request.setAttribute("pro",problem);


    List<String> languages;
    languages = problemDao.findLanguages(problem.getPid());

    request.setAttribute("languages",languages);
    request.setAttribute("pid",problem.getPid());
%>
<!DOCTYPE html>
<html lang="en"  style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${pro.title}</title>
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../../js/bootstrap.min.js"></script>
    <script src="../../js/showdown.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="icon" type="image/png" sizes="144x144" href="../../imgs/logo_blue_144.png"/>
    <link rel="stylesheet" href="./css/content.css">
    <!--文本编辑器-->
<!--    <script src="js/ext-language_tools.min.js"></script>-->
    <script src="https://cdn.bootcss.com/ace/1.4.6/ace.js"></script>
    <script src="https://cdn.bootcss.com/ace/1.4.6/ext-beautify.js"></script>
    <script src="https://cdn.bootcss.com/ace/1.4.6/ext-language_tools.js"></script>
    <script src="https://cdn.bootcss.com/ace/1.4.6/mode-javascript.js"></script>
    <script src="https://cdn.bootcss.com/ace/1.4.6/theme-xcode.js"></script>

    <link rel="stylesheet" href="https://unpkg.com/mditor@1.0.5/dist/css/mditor.min.css" />
    <script src="https://unpkg.com/mditor@1.0.5/dist/js/mditor.min.js"></script>
    <script src="http://cdn.bootcss.com/mathjax/2.7.0/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>

    <script type="text/x-mathjax-config">
    MathJax.Hub.Config({
        tex2jax: {inlineMath: [['$', '$']]},
        messageStyle: "none"
    });
    </script>
    <script src="js/content.js"></script>
</head>
<body style="padding-top: 60px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);
    min-width: 800px" hidden>
<%
    String path = problem.getPath() + "/main.md";
    String realPath = application.getRealPath(path);

    String text = ReadFileData.txt2String(new File(realPath));
    request.setAttribute("text",text);

    User_password user = (User_password) request.getSession().getAttribute("User");

    codeInfo codeInfo = null;
    if(user != null) codeInfo = problemDao.findCode(problem.getPid(),user.getId());
    String code = "",language = "";
    if(codeInfo != null){
        code = codeInfo.getCode();
        language = codeInfo.getLanguage();
    }
    request.setAttribute("code",code);
    request.setAttribute("code_language",language);

%>
<div hidden id="text">${text}</div>
<div id="header"></div>
<div class="main">
    <div class="row container">
        <div class="left col-md-10">
            <h1 style="text-align: center;">${pro.title}</h1>
            <div id="pro_main"></div>
        </div>
        <div class="right col-md-2" id="info">
            <table class="table inf">
                <tr>
                    <td>时间限制</td>
                    <td>${pro.time_limit}s</td>
                </tr>
                <tr>
                    <td>空间限制</td>
                    <td>${pro.memory_limit}M</td>
                </tr>
                <tr>
                    <td>评分</td>
                    <td>${pro.ranting}</td>
                </tr>
                <tr>
                    <td>通过率</td>
                    <td><fmt:formatNumber type="number" value="${r}" maxFractionDigits="2"/>%（${ac}/${sum}）  </td>
                </tr>
            </table>
            <a class="" id="sum" href="${pageContext.request.contextPath}/status?pid=${pid}"> 所有提交</a>
            <a class="" id="ans" href="../solution?pid=${pid}"> 查看题解</a>
        </div>
    </div>
    <div class="ide">
        <div class="bored_r"></div>
        <div class="top">
            <div class="glyphicon glyphicon-console" style="font-size: 30px;padding-left: 50px;padding-top: 10px"></div>
            <select class="form-control laguage" id="languages">
                <c:forEach items="${languages}" var="language" varStatus="sta">
                    <c:if test="${fn:length(code_language) == 0}">
                        <c:if test="${sta.index == 0}">
                            <option selected>${language}</option>
                        </c:if>
                        <c:if test="${sta.index != 0}">
                            <option>${language}</option>
                        </c:if>
                    </c:if>
                    <c:if test="${fn:length(code_language) != 0}">
                        <c:if test="${language == code_language}">
                            <option selected>${language}</option>
                        </c:if>
                        <c:if test="${language != code_language}">
                            <option>${language}</option>
                        </c:if>
                    </c:if>
                </c:forEach>
                <%--                <option>C</option>--%>
                <%--                <option>JAVA</option>--%>
                <%--                <option>Python</option>--%>
            </select>
        </div>
        <pre id="code" class="ace_editor"><textarea class="ace_text-input">${code}</textarea></pre>
        <div class="end">
            <p style="font-size: 25px;padding-left: 5%;padding-top: 2%">运行状态：<span id="sta"></span></p>
            <button type="button" id="run" data-loading-text="运行中..." class="btn btn-default" autocomplete="off">
                运行
            </button>
            <%--            <input class="btn btn-default" type="submit" id="run" value="运行">--%>
            <button type="button" id="sub" data-loading-text="提交中..." class="btn btn-default" autocomplete="off">
                提交
            </button>
            <%--            <input class="btn btn-default" type="submit" id="sub" value="提交">--%>
        </div>
        <div class="code">
            <div class="input">
                输入
                <textarea rows="1" id="run-code-stdin" class="form-control autofit" maxlength="2000" style="background-color: rgb(248, 248, 248); border-radius: 5px; overflow:hidden; resize: none; font-family: monospace;min-height:35px"></textarea>
            </div>
            <div class="output">
                输出
                <pre id="run-code-stdout" style="background-color: #f8f8f8; border-radius: 5px; margin-top: 5px; min-height: 35px;" class="hljs">
</pre>
            </div>
        </div>
    </div>
</div>
<div hidden id="path">${pageContext.request.contextPath}</div>
<div hidden id="pid">${requestScope.pid}</div>
<div id="footer"></div>
</body>
</html>