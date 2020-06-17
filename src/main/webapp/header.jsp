<%@page contentType="text/html;charset=UTF-8" language="java" %>
<script>
    var path,id = null;
    function open_register() {
        $("#login").modal('hide');
        $("#register").modal('show');
        // $("#code2").attr("src",path + "/checkCode?"+new Date().getTime());
        change_check2();
    }
    function open_login() {
        $("#login_msg").hide();
        $("#login").modal('show');
        $("#register").modal('hide');
        // $("#code1").attr("src",path + "/checkCode?"+new Date().getTime());
        change_check();
    }
    function changeCheckCode(img) {
        img.src=path + "/checkCode?"+new Date().getTime();
    }
    function show_login_info(user) {
        var user_img_url = path + user.head_img;
        $("#head_img").attr("src",user_img_url);
        $("#user_name").html(user.name);
        $("#user_lv").html(user.lv);
        $("#user_ranting").html("LV"+user.ranting);
        $("#login_btn").css("display","none");
        $("#register_btn").css("display","none");
        $("#head_img").show();
    }
    function change_check(){
        changeCheckCode(document.getElementById("code1"));
    }
    function change_check2(){
        changeCheckCode(document.getElementById("code2"));
    }
    function init_page_header(){
        $.ajax({
            url: path+"/userServlet/findUserServlet",
            // 将XHR对象的withCredentials设为true
            xhrFields: {
                withCredentials: true
            },
            type:'post',
            success:function (data) {
                if(data.success){
                    id=data.user.id;
                    show_login_info(data.user);
                    return;
                }

                $("#head_img").hide();
                $("#register_btn").css("display","");
                $("#login_btn").css("display","");
            }
        });
    }
    function validateEmail (email) {
        // 邮箱验证正则
        var reg = /^[A-Za-z0-9]+([_\.][A-Za-z0-9]+)*@([A-Za-z0-9\-]+\.)+[A-Za-z]{2,6}$/;
        return reg.test(email);
    }
    function changeMenu(){
        $(".dropdown-menu").dropdown('toggle');
    }
    function jump_user_info(){
        if(id == null) window.location.href = path + "/error";
        else{
            window.location.href = path + "/user?id="+id;
        }
    }

    $(function () {
        path = $("#path").html();
        init_page_header();
        $("#dropdown-img").mouseover(function () {
            $(".dropdown-menu").dropdown('toggle');
        })
        $("#dropdown-img").mouseout(function () {
            $(".dropdown-menu").dropdown('toggle');
        })
        $("#user_login").click(function () {
            $("#login_msg").hide();
            var user_id = $("#input_id_email").val();
            var user_password = $("#inputPassword").val();
            var check_code = $("#user_check_code").val();
            if(user_id.length==0){
                $("#login_msg").html("请输入用户名");
                $("#login_msg").show();
                change_check();
                return;
            }
            if(user_id.length>20) {
                $("#login_msg").html("请输入正确的用户名");
                $("#login_msg").show();
                change_check();
                return;
            }
            if(user_password.length==0){
                $("#login_msg").html("请输入密码");
                $("#login_msg").show();
                change_check();
                return;
            }
            if(user_password.length>15){
                $("#login_msg").html("用户名或密码错误");
                $("#login_msg").show();
                change_check();
                return;
            }

            if(check_code.length==0){
                $("#login_msg").html("请输入验证码");
                $("#login_msg").show();
                change_check();
                return;
            }

            $.ajax( {
                url:path+"/userServlet/loginServlet",// 跳转到
                data:{
                    user_id : user_id,
                    user_password : user_password,
                    check_code : check_code,
                },
                type:'post',
                cache:false,
                dataType:'json',
                success:function(data) {
                    if(data.success){
                        location.reload();
                    }else{
                        $("#login_msg").html(data.msg);
                        $("#login_msg").show();
                        change_check();
                    }
                },
                error : function() {
                    // view("异常！");
                    console.log("异常！");
                }
            });
        })
        $("#user_out").click(function () {
            $.ajax({
                url: path+"/userServlet/exitServlet",
                // 将XHR对象的withCredentials设为true
                xhrFields: {
                    withCredentials: true
                },
                type:'post',
                success:function () {
                    location.reload();
                }
            });

        })
        $("#user_register").click(function () {
            $("#success_msg").hide();
            $("#register_msg").hide();
            /*
                name昵称
                stuid学号
                user_password密码
                user_register_check_code验证码
                user_email 邮箱
                register_msg
            */

            var name = $("#name").val();
            var uid = $("#stuid").val();
            var password = $("#user_password").val();
            var password2 = $("#user_password2").val();
            var check_code = $("#user_register_check_code").val();
            var user_email = $("#user_email").val();

            if(name.length==0){
                $("#register_msg").html("昵称不能为空");
                $("#register_msg").show();
                return;
            }
            if(name.length>10){
                $("#register_msg").html("昵称过长");
                $("#register_msg").show();
                return;
            }
            if(uid.length==0){
                $("#register_msg").html("学号不能为空");
                $("#register_msg").show();
                return;
            }
            if(uid.length>20){
                $("#register_msg").html("学号过长");
                $("#register_msg").show();
                return;
            }
            if(password.length==0){
                $("#register_msg").html("密码不能为空");
                $("#register_msg").show();
                return;
            }
            if(password != password2){
                $("#register_msg").html("两次密码不一致");
                $("#register_msg").show();
                return;
            }
            if(password.length>15){
                $("#register_msg").html("密码过长");
                $("#register_msg").show();
                return;
            }
            if(check_code.length==0){
                $("#register_msg").html("请输入验证码");
                $("#register_msg").show();
                return;
            }
            if(user_email.length==0){
                $("#register_msg").html("请输入邮箱");
                $("#register_msg").show();
                return;
            }
            if(!validateEmail(user_email)){
                $("#register_msg").html("邮箱格式不正确");
                $("#register_msg").show();
                return;
            }

            $.ajax( {
                url:path+"/userServlet/registerServlet",// 跳转到
                /*
                name昵称
                stuid学号
                user_password密码
                user_register_check_code验证码
                register_msg
            */
                data:{
                    name : name,
                    uid : uid,
                    password:password,
                    check_code : check_code,
                    email:user_email,
                },
                type:'post',
                cache:false,
                dataType:'json',
                success:function(data) {
                    if(data.success){
                        $("#success_msg").show();
                        alert("注册成功");
                        open_login();
                        change_check2();
                    }else{
                        $("#register_msg").html(data.msg);
                        $("#register_msg").show();
                        change_check2();
                    }
                },
                error : function() {
                    // view("异常！");
                    console.log("异常！");
                }
            });
        })
    });

</script>
<style>
    #search{
        color:#9d9d9d;
        font-size: 18px;
    }
    #head_img{
        width: 45px;

    }
    #head_img:hover,#dLabel:hover{
        border: 2px solid rgba(204,204,204, 0.2);
    }
</style>

<div hidden id="path">${pageContext.request.contextPath}</div>
<!-- 头部 start -->
<header id="header">
    <nav class="navbar-inverse navbar-fixed-top">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand">Online Judge</a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->

            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li id="home"><a href="${pageContext.request.contextPath}/">首页</a></li>
                    <li id="problem"><a href="${pageContext.request.contextPath.concat("/problem")}">题库</a></li>
                    <li id="contests"><a href="${pageContext.request.contextPath.concat("/contests")}">比赛</a></li>
                    <li id="status"><a href="${pageContext.request.contextPath.concat("/status")}">提交记录</a></li>
                    <li id="rank"><a href="${pageContext.request.contextPath.concat("/rank")}">排名</a></li>
                    <li id="special"><a href="${pageContext.request.contextPath.concat("/special")}">专题训练</a></li>
                    <li id="CF"><a href="${pageContext.request.contextPath.concat("/CF")}">CF</a></li>
                </ul>
<!--                <a href="javascript:void(0)" class="glyphicon glyphicon-search" id="search"></a>-->
                <!--                <a class="glyphicon glyphicon-search" style="color: white;font-size: 20px;" aria-hidden="true"></a>-->
                <ul class="nav navbar-nav navbar-right">
                    <li id="dropdown-img">
                        <div class="dropdown">
                            <a href="javascript:void(0);" style="padding: 0px;background-color: black;"
                               data-toggle="dropdown"
                               onclick='jump_user_info()';
                            >
                                <img hidden id="head_img" src="" class="img-circle" style="width: 40px;height: 40px">
                            </a><%--                            <button id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">--%>
<%--                                Dropdown trigger--%>
<%--                                <span class="caret"></span>--%>
<%--                            </button>--%>
                            <ul class="dropdown-menu"style="text-align: center;margin-right: -60px" onclick="changeMenu();">
                                <p><span id="user_name" style="
                                    margin-left: auto;
                                    margin-right: auto;
                                    overflow: hidden;
                                    text-overflow: ellipsis;
                                    -o-text-overflow: ellipsis;
                                    white-space:nowrap;
                                    width:7em;
                                    display:block;
                                 "></span></p>
                                <p style="color: black">等级：<span id="user_lv"></span></p>
                                <p style="color: black">评分：<span  id="user_ranting"></span></p>
                                <button style="background-color: #ddd" class="btn btn-default glyphicon glyphicon-log-out" id="user_out"></button>
                            </ul>
                        </div>
                    </li>
                    <li style="display: none" id="login_btn"><a href="javascript:void(0)" onclick="open_login()">登录</a></li>
                    <li style="display: none" id="register_btn"><a href="javascript:void(0)" onclick="open_register()">注册</a></li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
</header>
<!-- 头部 end -->
<!-- 首页导航 -->
<div class="navitem"></div>

<!-- Modal -->
<div class="modal fade" id="login" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span style="font-size: 20px;padding-left: 20px;color:#000" class="modal-title">登录</span>
                <span hidden style="padding-left: 30%;color: red" id="login_msg"></span>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="input_id_email" class="col-sm-2 control-label" style="color:#000">用户名：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="input_id_email"  placeholder="学号/邮箱">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPassword" class="col-sm-2 control-label" style="color:#000">密码：</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="inputPassword" placeholder="密码">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPassword" class="col-sm-2 control-label" style="margin-top: 10px;color:#000">验证码：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="user_check_code" style="margin-top: 10px">
                        </div>
                        <div class="col-sm-6">
                            <img src="" id="code1"  style="width: 150px;border: #ddd solid 1px" alt="" onclick="changeCheckCode(this)">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="submit" id="user_login" class="btn btn-default">登录</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0);" onclick="open_register()">没有账号去注册</a>
                <a href="javascript:void(0);">忘记密码</a>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="register" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span style="font-size: 20px;padding-left: 20px;color:#000" class="modal-title">注册</span>
                <span hidden style="padding-left: 30%;color: red" id="register_msg"></span>
                <span hidden style="padding-left: 30%;color:#000" id="success_msg">注册成功！</span>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="name" class="col-sm-3 control-label" style="color:#000">昵称：</label>
                        <div class="col-sm-9">
                            <input type="email" class="form-control" id="name" placeholder="昵称">
                        </div>
                    </div>
                </div>
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="stuid" class="col-sm-3 control-label" style="color:#000">学号：</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="stuid" placeholder="学号">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="user_password" class="col-sm-3 control-label" style="color:#000">密码：</label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="user_password" placeholder="由4~15位字符组成">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="user_password" class="col-sm-3 control-label" style="color:#000">重复密码：</label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="user_password2" placeholder="由4~15位字符组成">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="user_email" class="col-sm-3 control-label" style="color:#000">邮箱：</label>
                        <div class="col-sm-9">
                            <input type="email" class="form-control" id="user_email" placeholder="邮箱">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="user_register_check_code" class="col-sm-3 control-label" style="margin-top: 10px;color:#000">验证码：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="user_register_check_code" style="margin-top: 10px">
                        </div>
                        <div class="col-sm-5">
                            <img src="" id="code2" style="width: 150px;border: #ddd solid 1px" alt="" onclick="changeCheckCode(this)">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10" style="padding-left: 15%;">
                            <button type="submit" class="btn btn-default" id="user_register">注册</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0);" onclick="open_login()">已有帐号去登录</a>
            </div>
        </div>
    </div>
</div>