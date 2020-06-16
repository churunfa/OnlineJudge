<%@ page import="OnlineJudge.dao.impl.ProblemDaoImpl" %>
<%@ page import="OnlineJudge.dao.impl.ContestsDaoImpl" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@ page import="OnlineJudge.domain.Contest" %>
<%@ page import="OnlineJudge.domain.User_password" %>
<%@ page import="java.util.List" %>
<%@ page import="com.alibaba.druid.sql.visitor.functions.Char" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="OnlineJudge.util.ReadFileData" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="OnlineJudge.dao.TagDao" %>
<%@ page import="OnlineJudge.dao.impl.TagDaoImpl" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String pid = request.getParameter("pid");
    String cid = request.getParameter("cid");

    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();

    Problem pro = null;
    Contest con = null;

    if(pid != null) pro = problemDao.findProblemByPid(Integer.parseInt(pid));
    if(cid != null) con = contestsDao.findContestByCid(Integer.parseInt(cid));

    if(pro == null && con == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }
    if(pro != null && con != null){
        if(pro.getContest_id() != con.getId()){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }
    }

    request.setAttribute("cid",cid);
    request.setAttribute("pid",pid);

    String title = "添加题目";

    User_password user = (User_password) request.getSession().getAttribute("User");
    if(user == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    int flag=0;
    if(pro != null){
        if(pro.getMaster() != user.getId() && !"root".equals(user.getPower())){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }
        con = contestsDao.findContestByCid(pro.getContest_id());

        title = pro.getTitle();
        request.setAttribute("flag",1);

        flag = 1;
        request.setAttribute("pro",pro);

        String path = pro.getPath();
        String realPath = application.getRealPath(path);

        File pro_text_file = new File(realPath + "/main.md");
        String pro_text = "";
        if(pro_text_file.exists()) pro_text = ReadFileData.txt2String(pro_text_file,false);
        request.setAttribute("pro_text",pro_text);

        File codeBaseFile = new File(realPath + "/codeBase", "code");
        String codeBase = "";
        if(codeBaseFile.exists()) codeBase = ReadFileData.txt2String(codeBaseFile, false);
        request.setAttribute("codeBase",codeBase);

        File interactiveFile = new File(realPath + "/interactive", "interactive.cpp");
        String interactive = "";
        if(interactiveFile.exists()) interactive = ReadFileData.txt2String(interactiveFile, false);
        request.setAttribute("interactive",interactive);

        String dataPath = application.getRealPath("/data")+"/"+pro.getPid();

        File spjFile = new File(dataPath + "/spj", "spj.cc");
        String spj = "";
        if(spjFile.exists()) spj = ReadFileData.txt2String(spjFile, false);
        request.setAttribute("spj",spj);
        Pattern p_in = Pattern.compile("simple\\d\\.in");
        Pattern p_out = Pattern.compile("simple\\d\\.out");

        File data1 = new File(dataPath);

        File[] files = data1.listFiles();
        List<String> data_in = new ArrayList<String>();
        List<String> data_out = new ArrayList<String>();

        if(files != null){
            for (File file:files){
                String name = file.getName();
                String s = null;
                if(file.exists() && file.isFile()) s = ReadFileData.txt2String(file, false);
                Matcher m_in = p_in.matcher(name);
                Matcher m_out = p_out.matcher(name);
                if(s != null && m_in.find())data_in.add(s);
                if(s != null && m_out.find()) data_out.add(s);
            }
        }

        request.setAttribute("data_in",data_in);
        request.setAttribute("data_out",data_out);
        int data1_sum = Math.min(data_in.size(),data_out.size());
        request.setAttribute("data1_sum",data1_sum);

        File data2 = new File(dataPath);
        int sum_in = 0,sum_out = 0;
        String[] data2s = data2.list();


        if(data2s != null){
            for(String data:data2s){
                Pattern p_in2 = Pattern.compile("\\.in");
                Pattern p_out2 = Pattern.compile("\\.out");
                Matcher m_in = p_in2.matcher(data);
                Matcher m_out = p_out2.matcher(data);
                if(m_in.find()) sum_in++;
                if(m_out.find()) sum_out++;
            }
        }

        request.setAttribute("sum",Math.max(sum_in,sum_out));

        List<String> languages = problemDao.findLanguages(pro.getPid());

        HashMap<String,Integer> mp = new HashMap<String,Integer>();
        mp.put("C",1);
        mp.put("C++",2);
        mp.put("Python",3);
        mp.put("JAVA",4);

        for(String language:languages) request.setAttribute("language" + mp.get(language),true);


        List<String> tags = new ArrayList<String>();

        TagDaoImpl tagDao = new TagDaoImpl();
        List<Integer> allTag = tagDao.findAllTag(pro.getPid());
        for(int tag:allTag){
            String tag_name = (String) tagDao.findTag(tag);
            tags.add(tag_name);
        }
        request.setAttribute("tags",tags);
        request.setAttribute("pid",pro.getPid());
        request.setAttribute("cid",pro.getContest_id());
    }else if(con != null){
        if(con.getMaster() != user.getId() && !"root".equals(user.getPower())){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }
        request.setAttribute("flag",2);
        flag = 2;
        request.setAttribute("cid",con.getId());
    }

    request.setAttribute("title",title);

    List<Integer> pidByCid = problemDao.findPidByCid(con.getId());
    int sum = pidByCid.size();

    String s="";

    List<Integer> number = new ArrayList<Integer>();
    number.add(0);
    for (int i = 0; i < sum ; i ++ )
    {
        number.set(0,number.get(0)+1);
        for (int j = 0; number.get(j) == 26; j ++ )
        {
            number.set(j,0);
            if (j + 1 == number.size()) number.add(0);
            else number.set(j+1,number.get(j+1)+1);
        }
    }

    for (int i = number.size() - 1; i >= 0; i -- ) s = s + (char)('A' + number.get(i));

    if(flag == 2) request.setAttribute("show_id",s);
    else request.setAttribute("show_id",pro.getType());
    request.setAttribute("pro",pro);
    request.setAttribute("con",con);
%>

<!DOCTYPE html>
<html lang="en" style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="../../css/bootstrap.min.css">
    <script src="../../js/jquery-1.11.0.min.js"></script>
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
    <link rel="stylesheet" href="./css/problem_add.css">
    <script src="js/problem_add.js"></script>

    <!--Markdown-->
    <link rel="stylesheet" href="../../js/editor.md-master/css/editormd.css">
    <link rel="stylesheet" href="../../js/editor.md-master/examples/css/style.css">
    <script src="../../js/editor.md-master/editormd.min.js"></script>


</head>
<body style="padding-top: 60px;
    min-width: 800px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);"  hidden>
<div id="header"></div>
<div class="container main">
    <div hidden id="cid">${cid}</div>
    <div hidden id="pid">${pid}</div>
    <div hidden id="path">${pageContext.request.contextPath}</div>
    <div class="row" style="margin-top: 20px">
        <div class="col-md-3">
            <input type="text" class="form-control" placeholder="题目ID" id="show_id" value="${show_id}">
        </div>
        <div class="col-md-9">
            <input type="text" class="form-control" placeholder="题目名称" id="title" hidden
            <c:if test="${flag == 1}"> value="${pro.title}" </c:if>

            >
        </div>
    </div>
    <div>
        <div id="desc" style="margin-top:50px;">
            <textarea style="display:none;" id="pro_text"><c:if test="${flag == 1}">${pro_text}</c:if><c:if test="${flag != 1}">${pro_text}## 问题描述：
	balabala
## 输入：
	balabala
## 输出：
	balabala
## 数据范围：
例：
$$1 \leq n \leq 10^5, 1\leq a \leq 10^9$$
## 输入样例1：
```
1 2
```
## 输出样例1
```
3
```</c:if></textarea>
        </div>
    </div>
    <div class="row div-css">
        <h4 class="" style="text-align: left;margin-bottom: 20px;margin-left: 20px">选择允许使用的编程语言：</h4>
        <label class="checkbox-inline">
            <input type="checkbox" id="language_c" value="C" <c:if test="${flag == 1 && language1==true || flag != 1}">checked</c:if>> C
        </label>
        <label class="checkbox-inline">
            <input type="checkbox" id="language_c_plus" value="C++"  <c:if test="${flag == 1 && language2==true || flag != 1}">checked</c:if>> C++
        </label>
        <label class="checkbox-inline">
            <input type="checkbox" id="language_python" value="Python"  <c:if test="${flag == 1 && language3==true || flag != 1}">checked</c:if>> Python
        </label>
        <label class="checkbox-inline">
            <input type="checkbox" id="language_java" value="JAVA"  <c:if test="${flag == 1 && language4==true || flag != 1}">checked</c:if>> JAVA
        </label>
    </div>
    <div class="row div-css" >
        <div class="col-md-6">
            <table>
                <tr>
                    <td><h4>时间限制（秒）：</h4></td>
                    <td>
                        <input class="form-control" id="time_limit"
                            <c:if test="${flag == 1}">value = "${pro.time_limit}"</c:if>
                            <c:if test="${flag != 1}">value="1000"</c:if>
                        >
                    </td>
                </tr>
            </table>
        </div>
        <div class="col-md-6">
            <table>
                <tr>
                    <td><h4>空间限制（M）：</h4></td>
                    <td>
                        <input class="form-control" id="memory_limit"
                               <c:if test="${flag == 1}">value = "<fmt:formatNumber value="${pro.memory_limit}" type="number"/>"</c:if>
                               <c:if test="${flag != 1}">value="64"</c:if>
                        >
                    </td>
                </tr>
            </table>
        </div>

    </div>
    <div class="row div-css" hidden>
        <h4 class="" style="text-align: left;margin-bottom: 20px;margin-left: 20px">是否添加代码模板:
            <img id="code_template_switch_close" src="../../imgs/icons_close.png" alt="switch-close" style="width: 40px;margin-top: -2px">
            <img id="code_template_switch_open" hidden src="../../imgs/icons_open.png" alt="switch-open" style="width: 40px;margin-top: -2px">
        </h4>
        <pre hidden id="code_template_pre" style="border-width: 0;background-color:rgba(255, 255, 255, 0);" >
            <textarea class="form-control" id="code_template" rows="16"><c:if test="${flag == 1}">${codeBase}</c:if></textarea>
        </pre>
    </div>
    <div class="div-css" id="simple-data">
        <h4 style="text-align: left;margin-bottom: 20px;margin-left: 20px">添加简单测试数据：
            <button class="btn btn-default glyphicon glyphicon-plus" style="font-size: 8px;margin-top: -5px" type="submit" id="add_data"></button>
        </h4>
        <c:if test="${flag == 1}">
            <div hidden id="data_sum">${data1_sum}</div>
            <c:forEach begin="1" end="${data1_sum}" var="i">
                <div id="simple-data${i+1}" class="row data">
                    <div class="col-md-5">
                       <textarea type="text" rows="6" class="form-control data-in" placeholder="输入">${data_in[i-1]}</textarea>
                    </div>
                    <div class="col-md-5">
                        <textarea type="text" rows="6" class="form-control data-out" placeholder="输出">${data_out[i-1]}</textarea>
                    </div>
                    <div class="col-md-2" style="text-align: left">
                        <button class="btn btn-default glyphicon glyphicon-remove"style="font-size: 8px;text-align: left" onclick="del(${i})"></button>
                    </div>
                </div>
            </c:forEach>

        </c:if>
<!--        <div id="simple-data1" class="row data">-->
<!--            <div class="col-md-5">-->
<!--                <textarea type="text" rows="6" class="form-control data-in" placeholder="输入"></textarea>-->
<!--            </div>-->
<!--            <div class="col-md-5">-->
<!--                <textarea type="text" rows="6" class="form-control data-out" placeholder="输出"></textarea>-->
<!--            </div>-->
<!--            <div class="col-md-2" style="text-align: left">-->
<!--                <button class="btn btn-default glyphicon glyphicon-remove"style="font-size: 8px;text-align: left" onclick="del(1)"></button>-->
<!--            </div>-->
<!--        </div>-->

    </div>
    <div class="row div-css" >
        <h4 class="col-md-2" style="text-align: left;margin-bottom: 20px">上传测试数据：</h4>
        <input class="col-md-3" style="font-size: 15px" type="file" id="dataFile">
        <p class="col-md-3">已上传<span><c:if test="${flag == 1}">${sum}</c:if></span>组数据</p>
    </div>
    <div class="row div-css" >
        <h4 class="" style="text-align: left;margin-bottom: 20px;margin-left: 20px">是否开启spj:
            <img id="code_spj_switch_close" src="../../imgs/icons_close.png" alt="switch-close" style="width: 40px;margin-top: -2px">
            <img id="code_spj_switch_open" hidden src="../../imgs/icons_open.png" alt="switch-open" style="width: 40px;margin-top: -2px">
        </h4>
        <pre hidden id="code_spj_pre" style="border-width: 0;background-color:rgba(255, 255, 255, 0);" >
            <textarea class="form-control" id="code_spj" rows="16"><c:if test="${flag == 1}">${spj}</c:if></textarea>
        </pre>
    </div>
    <div class="row div-css" hidden>
        <h4 class="" style="text-align: left;margin-bottom: 20px;margin-left: 20px">是否添加交互函数:
            <img id="code_interactive_switch_close" src="../../imgs/icons_close.png" alt="switch-close" style="width: 40px;margin-top: -2px">
            <img id="code_interactive_switch_open" hidden src="../../imgs/icons_open.png" alt="switch-open" style="width: 40px;margin-top: -2px">
        </h4>
        <pre hidden id="code_interactive_pre" style="border-width: 0;background-color:rgba(255, 255, 255, 0);" >
            <textarea class="form-control" id="code_interactive" rows="16"><c:if test="${flag == 1}">${interactive}</c:if></textarea>
        </pre>
    </div>
    <div class="row div-css" >
        <table>
            <tr>
                <td>
                    <input class="form-control"  placeholder="添加题目标签" id="add_tag">
                </td>
                <td>
                    <button class="btn btn-default glyphicon glyphicon-ok" style="font-size: 11px;margin-top: 0px" type="submit" id="tag-btn"></button>
                </td>
            </tr>
        </table>


        <div id="tag">
            <div style="padding: 15px; overflow:hidden;" id="tags">
                <c:if test="${flag == 1}">
                    <div hidden id="tag_sum">${tags.size()}</div>
                    <c:forEach items="${tags}" var="tag" varStatus="sta">
                        <a class="tag-text" id="tag${sta.index + 1}" onclick="del_tag(${sta.index + 1})">${tag}</a>
                    </c:forEach>
                </c:if>
<!--                    <a id="tag1" class="" onclick="del_tag(1)">数据结构</a>-->
            </div>
        </div>
    </div>
    <div>
        <button style="background-image: linear-gradient(to bottom,#337ab7 0,#265a88 100%);" type="button" id="submit" data-loading-text="提交中..." class="btn btn-primary" autocomplete="off">
            提交
        </button>
    </div>
</div>
<div id="footer"></div>
</body>
</html>