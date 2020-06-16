<%@ page import="OnlineJudge.service.impl.UserServiceImpl" %>
<%@ page import="OnlineJudge.domain.User" %>
<%@ page import="OnlineJudge.service.UserService" %>
<%@ page import="OnlineJudge.domain.Problem" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.util.regex.Pattern" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en"  style="min-width: 800px;">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>用户信息</title>
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="../js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="../js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="./js/user.js"></script>
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
    <link rel="stylesheet" href="./css/user.css">
    <script src="../js/echarts.min.js"></script>
</head>
<body style="padding-top: 60px;
    background-image: linear-gradient(#E0E0E0, #D8BFD8);
    min-width: 800px;
"  hidden>

<%--初始化页面，若没有传入id则直接跳转错误页面--%>
<%
    String id = request.getParameter("id");
    if(id == null) {
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }

    User user = new UserServiceImpl().find(id);
    if(user == null){
        request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
        return;
    }
    String re = ">(.*?)<";

    String name ="";
    Matcher matcher = Pattern.compile(re).matcher(user.getName());
    while(matcher.find()) name= matcher.group();
    name = name.substring(1,name.length()-1);
    request.setAttribute("name",name);
    request.setAttribute("User",user);
%>

<%
    UserService userService = new UserServiceImpl();
    List<Problem> acs = userService.findStaById("答案正确", id);
    List<Problem> was = userService.findNotStaById("答案正确", id);

    List<Problem> real_wa = new ArrayList<Problem>();

    for(Problem wa:was){
        if(!wa.isIs_show()) continue;
        boolean flag = true;
        for(Problem ac:acs) if(wa.getPid() == ac.getPid()){
            flag =false;
            break;
        }
        if(flag) real_wa.add(wa);
    }

    List<Problem> acs_ = new ArrayList<Problem>();

    for(Problem ac:acs) if(ac.isIs_show()) acs_.add(ac);

    request.setAttribute("acs",acs_);
    request.setAttribute("was",real_wa);

    int contestByMaster = userService.findContestByMaster(id);
    request.setAttribute("contestMaster",contestByMaster);

%>

<div id="header"></div>
<div class="main_ container">
    <div class="row">
        <div class="col-md-4">
            <c:if test="${sessionScope.User.id == requestScope.User.id}">
                <div class="img_div" title="点击更换头像">
                    <img class="head" src="${pageContext.request.contextPath}${User.head_img}" alt="" width="350px" height="350px">
                </div>
            </c:if>
            <c:if test="${sessionScope.User.id != requestScope.User.id}">
                <div class="img_div"><img class="head" src="${pageContext.request.contextPath}/${User.head_img}" alt="" width="350px" height="350px">
                </div>
             </c:if>

            <div id="wa" style="margin-top: 30px">
                <p style="padding-left: 20px">待补题列表：<span>${fn:length(was)}</span></p>
                <div style="padding: 15px; overflow:hidden;">
                    <c:if test="${fn:length(was)==0}">
                        暂时没有需要补的题欧~
                    </c:if>
                    <c:if test="${fn:length(was)>6}">
                        <c:forEach begin="1" end="6" var="i" step="1">
                        <a href="${pageContext.request.contextPath}/problem/content/?pid=${was[i].pid}">#${was[i].contest_id}-${was[i].type} ${was[i].title}</a>
                        </c:forEach>
                    </c:if>
                    <c:if test="${fn:length(was)<=6}">
                        <c:forEach items="${was}" var="wa" >
                            <a href="${pageContext.request.contextPath}/problem/content/?pid=${wa.pid}">#${wa.contest_id}-${wa.type} ${wa.title}</a>
                        </c:forEach>
                    </c:if>
                </div>
                <c:if test="${fn:length(was)>6}">
                    <p id="more_wa">查看所有</p>
                </c:if>
            </div>
            <div id="tag">
                <p style="padding-left: 20px">通过题目：<span>${fn:length(acs)}</span></p>
                <div style="padding: 15px; overflow:hidden;">
                    <c:if test="${fn:length(acs)>6}">
                        <c:forEach begin="1" end="6" var="i" step="1">
                            <a href="${pageContext.request.contextPath}/problem/content/?pid=${acs[i].pid}">#${acs[i].contest_id}-${acs[i].type} ${acs[i].title}</a>
                        </c:forEach>
                    </c:if>
                    <c:if test="${fn:length(acs)<=6}">
                        <c:forEach items="${acs}" var="ac" >
                            <a href="${pageContext.request.contextPath}/problem/content/?pid=${ac.pid}">#${ac.contest_id}-${ac.type} ${ac.title}</a>
                        </c:forEach>
                    </c:if>
                </div>
                <c:if test="${fn:length(acs)>6}">
                    <p hidden id="more_ac">查看所有</p>
                </c:if>
            </div>
        </div>
        <div class="col-md-8">
            <div class="user_info">
                <p style="font-size: 50px;text-align: center">${User.name}<span style="font-size: 30px;padding-left: 30px;
                    color:
                <c:if test="${User.id !=1 && User.lv == 0}">#C9C9C9</c:if>
                <c:if test="${User.id !=1 &&User.lv == 1}">#7CFC00</c:if>
                <c:if test="${User.id !=1 &&User.lv == 2}">#7CCD7C</c:if>
                <c:if test="${User.id !=1 &&User.lv == 3}">#9B30FF</c:if>
                <c:if test="${User.id !=1 &&User.lv == 4}">#FFA500</c:if>
                <c:if test="${User.id !=1 &&User.lv == 5}">#FF0000</c:if>
                <c:if test="${User.id !=1 &&User.lv == 6}">#000</c:if>
                <c:if test="${User.id ==1}">#000</c:if>;

                    ">

                    LV${User.lv}

                </span></p>
                <div class="row">
                    <div class="col-md-6">
                        <p>评分：
                            <span style="color:
                            <c:if test="${User.id !=1 && User.lv == 0}">#C9C9C9</c:if>
                            <c:if test="${User.id !=1 &&User.lv == 1}">#7CFC00</c:if>
                            <c:if test="${User.id !=1 &&User.lv == 2}">#7CCD7C</c:if>
                            <c:if test="${User.id !=1 &&User.lv == 3}">#9B30FF</c:if>
                            <c:if test="${User.id !=1 &&User.lv == 4}">#FFA500</c:if>
                            <c:if test="${User.id !=1 &&User.lv == 5}">#FF0000</c:if>
                            <c:if test="${User.id !=1 &&User.lv == 6}">#000</c:if>
                            <c:if test="${User.id ==1}">#000</c:if>;
                            ">${User.ranting}</span></p>
                    </div>
                    <div class="col-md-6">
                        <p>最高评分：<span style="
                         color:
                        <c:if test="${User.id !=1 && User.lv == 0}">#C9C9C9</c:if>
                        <c:if test="${User.id !=1 &&User.lv == 1}">#7CFC00</c:if>
                        <c:if test="${User.id !=1 &&User.lv == 2}">#7CCD7C</c:if>
                        <c:if test="${User.id !=1 &&User.lv == 3}">#9B30FF</c:if>
                        <c:if test="${User.id !=1 &&User.lv == 4}">#FFA500</c:if>
                        <c:if test="${User.id !=1 &&User.lv == 5}">#FF0000</c:if>
                        <c:if test="${User.id !=1 &&User.lv == 6}">#000</c:if>
                        <c:if test="${User.id ==1}">#000</c:if>;
                        ">${User.max_ranting}</span></p>
                    </div>
                </div>

                <p>性别：<span>${User.sex}</span></p>
                <div class="row">
                    <div class="col-md-6">
                        <p>专业：<span>${User.major}</span></p>
                    </div>
                    <div class="col-md-6">
                        <p>班级：<span>${User.grade}</span></p>
                    </div>
                </div>
                <p>出题场数：<a href="">${contestMaster}场</a></p>

                <c:if test="${sessionScope.User.power == 'root'}">
                    <p>学号：<span>${User.uid}</span></p>
                </c:if>
                <c:if test="${sessionScope.User.power != 'root' && sessionScope.User.id == requestScope.User.id }">
<%--                    <p>学号：<span>${User.uid}</span></p>--%>
                </c:if>
                <p>邮箱：<span>${User.email}</span>
                    <c:if test="${User.status == false && sessionScope.User.id != requestScope.User.id}">
                        <span>（未认证）</span>
                    </c:if>
                    <c:if test="${User.status == false && sessionScope.User.id == requestScope.User.id}">
                        <a style="cursor: pointer" data-toggle="modal" data-target="#email_active" id="send_email">点击发送邮件进行认证</a>
                    </c:if>
                </p>

                <c:if test="${sessionScope.User.id == requestScope.User.id}">
                    <a style="font-size: 20px;cursor: pointer" data-toggle="modal" data-target="#update_user_info">修改个人信息</a>
                </c:if>
            </div>
            <div id="rating" style="width: 800px;height:600px;"></div>
        </div>
    </div>
</div>
<%--<!-- Button trigger modal -->--%>
<%--<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">--%>
<%--    Launch demo modal--%>
<%--</button>--%>

<!-- Modal -->
<div class="modal fade" id="update_user_info" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
<div class="modal-dialog" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">修改个人信息</h4>
        </div>
        <div class="modal-body">
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="inputName" class="col-sm-2 control-label">昵称</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="inputName" value="${name}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputUid" class="col-sm-2 control-label">学号</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="inputUid" value="${User.uid}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputMajor" class="col-sm-2 control-label">专业</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="inputMajor" value="${User.major}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputGrade" class="col-sm-2 control-label">班级</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="inputGrade" value="${User.grade}" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">性别</label>
                    <div class="col-sm-10">
                        <select class="form-control" id="sex">
                            <option value="男" <c:if test="${User.sex=='男'}">selected</c:if>>男</option>
                            <option value="女" <c:if test="${User.sex=='女'}">selected</c:if>>女</option>
                            <option value="保密" <c:if test="${User.sex=='保密'}">selected</c:if>>保密</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" id="submit_user_info">修改</button>
        </div>
    </div>
</div>
</div>

<!-- Modal -->
<div class="modal fade" id="email_active" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span style="font-size: 20px;padding-left: 20px" id="email_title-title">邮箱验证</span>
                <span hidden style="padding-left: 20%; color: red;" id="email_msg">验证码错误</span>
            </div>
            <div class="modal-body">
                <p>已发送验证码，请前往邮箱查收验证码</p>
                <input type="email" class="form-control" id="email_code" placeholder="请输入验证码">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="sub_btn">提交</button>
            </div>
        </div>
    </div>
</div>

<input style="display: none" type="file" id="load_img">
<div id="footer"></div>
<div id="prePath" hidden>${pageContext.request.contextPath}</div>
<div id="visit_id" hidden>${requestScope.User.id}</div>
</body>
</html>