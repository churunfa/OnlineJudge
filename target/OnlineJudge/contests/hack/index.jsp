<%@ page import="OnlineJudge.dao.impl.ProblemDaoImpl" %>
<%@ page import="java.io.File" %>
<%@ page import="OnlineJudge.util.ReadFileData" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@ page import="java.util.regex.Matcher" %><%--
  Created by IntelliJ IDEA.
  User: crf
  Date: 2020/6/14
  Time: 下午6:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pid = request.getParameter("pid");
    if(pid == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    ProblemDaoImpl problemDao = new ProblemDaoImpl();

    Problem pro = problemDao.findProblemByPid(Integer.parseInt(pid));
    if(pro == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    String dataPath = application.getRealPath("/data")+"/"+pro.getPid();

    Pattern p_in = Pattern.compile("hack-.*\\.in");
    Pattern p_out = Pattern.compile("hack-.*\\.out");

    File file = new File(dataPath);
    File[] datas = file.listFiles();

    int sum_in=0,sum_out=0;
    if(datas != null){
        for(File data:datas){
            String name = data.getName();

            Matcher m_in = p_in.matcher(name);
            Matcher m_out = p_out.matcher(name);
            if(m_in.find()) sum_in++;
            if(m_out.find()) sum_out++;
        }
    }

    request.setAttribute("pid",pid);
    request.setAttribute("sum",Math.min(sum_in,sum_out));
%>
<!DOCTYPE html>
<html lang="en" style="min-height: 100%;min-width: 800px">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>hack</title>
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="./js/hack.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="./css/hack.css">
    <link rel="icon" type="image/png" sizes="144x144" href="../../imgs/logo_blue_144.png"/>
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
<div hidden id="pid">${pid}</div>
<div hidden id="path">${pageContext.request.contextPath}</div>
<div class="container main">
    <div class="div-css" id="simple-data">

        <div class="jumbotron">
            <h2>您可以在这里提交hack数据hack其他人的代码</h2>
            <p>1.您提交的测试数据会通过标程进行测试，因此请为务必保证您的输出数据根标程的输出一模一样（包括空格和换行）</p>
            <p>2.因为需要通过标程对hank代码进行测试，所以数据提交过程可能较慢，请耐心等待</p>
            <p>3.hack数据一旦上传就不可撤销，上传之前请务必保证您的测试数据符合题目规定的数据范围</p>
            <p>4.比赛结束12小时后会将hack数据加入测试数据，并将所有代码重新进行重测</p>
        </div>

        <h4 style="text-align: left;margin-bottom: 20px;margin-left: 20px">添加hack数据：
            <button class="btn btn-default glyphicon glyphicon-plus" style="font-size: 8px;margin-top: -5px" type="submit" id="add_data"></button>
            <span>此题以累积提交<span id="sum">${sum}</span>组hack数据</span>
        </h4>

    </div>
<%--    <div class="row div-css" >--%>
<%--        <h4 class="col-md-2" style="text-align: left;margin-bottom: 20px">上传测试数据：</h4>--%>
<%--        <input class="col-md-3" style="font-size: 15px" type="file" id="dataFile">--%>
<%--    </div>--%>
    <button style="margin-left: 50%;margin-top: 70px" type="button" id="submit" data-loading-text="提交中..." class="btn btn-default" autocomplete="off">
        提交
    </button>
</div>
<div id="footer"></div>
</body>
</html>
