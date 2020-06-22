var path;
var st = 0;
var cid = 0;
var time = 0;
var flag = false;
var title;

function gao(){
    $.ajax( {
        url:path+"/waitServlet/timeAndDanMu",// 跳转到
        data:{
            id:cid,
            st:st,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                time = data.time;
                if(!flag) init();
                flag = true;
                st+=data.danMu.length;
                for(var i = 0;i <data.danMu.length;i++){
                    var imgPath = path+data.users[i].head_img;
                    var userZ = path+"/user?id="+data.users[i].id;
                    // var s = '<span><img src="'+imgPath+'" alt="" style="width: 25px">'+data.danMu[i].msg+'</span>'

                    var x = 10;
                    var y = 0;
                    var sss = new Date().getSeconds()*6

                    var col = ["#3fd316","#0dd2ef","#ff0000","#3fd316","#0dd2ef","#ffffff","#3fd316","#0dd2ef","#ff0000","#3fd316"];
                    // 随机颜色
                    var colors = parseInt(Math.random() * (x - y + 1) + y);
                    // 随机高度
                    var rand = parseInt(Math.random() * (x - y + 1) + y) * sss;
                    // 随机速度
                    var sudu = parseInt(Math.random() * (x - y + 1) + y) * 3;
                    // 设置最低速度，禁止为0
                    if(sudu < 1){
                        sudu = 10;
                    };
                    var dasdass = " animation: Barrag " + sudu + "s linear infinite;";
                    var dasdass2 = " -webkit-animation: Barrag " + sudu + "s linear infinite;";
                    var colorss = "color:" + col[colors] + ";";
                    var spans = "<span" + " style='top:" + rand + "px;" + colorss + dasdass + dasdass2 + "'>"+ "<img src='"+imgPath+"' style='width: 25px'>" + data.danMu[i].msg + "</span>";
                    $("#danMu").append(spans);
                }

            }else{
                location.href = path + "/error";
            }
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
        }
    });
}

function init(){
    //加入时钟数字元素
    var list = "<li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li>";
    var ool = "<ol>" + list + "</ol>";
    $(".spot").after(ool);
    //显示日期
    var date = "<div class='date'></div>";
    $(".spot").before(date);
    //显示星期
    var week = "<div class='week'></div>";
    $(".date").before(week);
    //显示年度
    var year = "<div class='year'></div>";
    $(".date").before(year);
    //显示时间
    var times = "<div class='times'></div>";
    $(".date").before(times);

    //比赛倒计时
    // var starttime = new Date(2020,4,10,13,30,35);
    var starttime = new Date(time);
    setInterval(function () {
        var nowtime = new Date();
        var time = starttime - nowtime;
        var day = parseInt(time / 1000 / 60 / 60 / 24);
        var hour = parseInt(time / 1000 / 60 / 60 % 24);
        var minute = parseInt(time / 1000 / 60 % 60);
        var seconds = parseInt(time / 1000 % 60);

        if(time<=0){
            location.href=path+"/contests/contests_show/?id="+cid;
        }
        var syday = "<span>" + hour + "</span>" + "小时" + "<span>" + minute + "</span>" + "分钟" + "<span class='Lose'>" + seconds + "</span>" + "秒";
        $('.time').html("<p>"+ $('#title').html() +"</p>" + "<p><span>" + day + "</span>" + "天</p>");
        $(".sydate").html(syday);

    }, 1000);

    // 时钟走字方法
    function Todayss(){
        var day = new Date();  //日期
        var Y = day.getFullYear();  //年
        var M = day.getMonth() + 1;  //月
        var D = day.getDate();  //日
        var U = day.getUTCDay();  //周
        var H = day.getHours();  //时
        var MIN = day.getMinutes();  //分
        var S = day.getSeconds();  //秒
        var MSs = day.getMilliseconds();
        var MS = MSs.toString().substring(0,2);
        // 计算指针度数
        var sss = S * 6,
            mmm = MIN * 6 + (sss * 0.01),
            hhh = (H * 30) + (MIN * 0.5);
        var rotss = "rotate(" + sss + "deg)";
        var rotmm = "rotate(" + mmm + "deg)";
        var rothh = "rotate(" + hhh + "deg)";
        $(".HH").css({"transform":rothh});
        $(".MM").css({"transform":rotmm});
        $(".SS").css({"transform":rotss});
        // 小于两位数,保持两位
        if(M < 10){M = "0" + M;};
        if(D < 10){D = "0" + D;};
        if(H < 10){H = "0" + H;};
        if(MIN < 10){MIN = "0" + MIN;};
        if(S < 10){S = "0" + S;};

        switch (U){
            case 0:U="星期日";
                break;
            case 1:U="星期一";
                break;
            case 2:U="星期二";
                break;
            case 3:U="星期三";
                break;
            case 4:U="星期四";
                break;
            case 5:U="星期五";
                break;
            case 6:U="星期六";
                break;
        };

        //星期赋值
        var week = U;
        $(".week").html(week);

        //年份赋值
        var year = Y;
        $(".year").html(year);

        //日期赋值
        var date = "<span>" + M + "</span>" + "月" + "<span>" + D + "</span>" + "日";
        $(".date").html(date);

        //时间赋值
        var times = "<span>" + H + "</span>" + ":" + "" + "<span>" + MIN + "</span>" + ":" + "" + "<span>" + S + "</span>" + ":" + "" + "<span>" + MS + "</span>";
        $(".times").html(times);

    }
    // Todayss();
    setInterval(Todayss, 10);


    // 发送弹幕
    $(".message").click(function(){
        $(".key").addClass("keys");
        $(".key").removeClass("remove");
    });

    function Closs(){
        $(".key").removeClass("keys");
        $(".key").addClass("remove");
        setTimeout(function(){
            $(".key").removeClass("remove");
        },1000);
    };

    $(".iocnBox").click(function(){
        Closs()
    });
    $(".today").click(function(){
        var ksyss = $(".key").hasClass("keys");
        if(ksyss == true){
            Closs()
        }
    });


    // var mess = "<span>哈喽啊, 少年！！</span><span class='B-span2'>比赛愉快！</span><span>Hello, 2020, happy new year</span>";
    // $(".Barrage").append(mess);

    $(".buts").click(function(){
        var mes = $(".van-field__control").val();
        if(!mes){
            // alert("你还没输入内容呢!")
            var Tipss = "<div class='Tipss'>您还没输入内容呢</div>";
            $("body").append(Tipss);
            setTimeout(function(){
                $(".Tipss").remove();
            },1500)
        }else{

            $.ajax( {
                url:path+"/waitServlet/submitDanMu",// 跳转到
                data:{
                    id:cid,
                    msg:mes,
                },
                type:'post',
                cache:false,
                dataType:'json',
                success:function(data) {
                    if(data.success){
                        var Tips = "<div class='Tips'>"+data.msg+"</div>";
                        $("body").append(Tips);
                        setTimeout(function(){
                            $(".Tips").remove();
                        },1500)
                    }else{
                        var Tips = "<div class='Tips'>"+data.msg+"</div>";
                        $("body").append(Tips);
                        setTimeout(function(){
                            $(".Tips").remove();
                        },1500)
                    }
                },
                error : function() {
                    // view("异常！");
                    console.log("异常！");
                }
            });

            //生成随机数: x上限，y下限
            // var x = 10;
            // var y = 0;
            //
            // var col = ["#3fd316","#0dd2ef","#ff0000","#3fd316","#0dd2ef","#ffffff","#3fd316","#0dd2ef","#ff0000","#3fd316"];
            // // 随机颜色
            // var colors = parseInt(Math.random() * (x - y + 1) + y);
            // // 随机高度
            // var rand = parseInt(Math.random() * (x - y + 1) + y) * sss;
            // // 随机速度
            // var sudu = parseInt(Math.random() * (x - y + 1) + y) * 3;
            // // 设置最低速度，禁止为0
            // if(sudu < 1){
            //     sudu = 10;
            // };
            // var dasdass = " animation: Barrag " + sudu + "s linear infinite;";
            // var dasdass2 = " -webkit-animation: Barrag " + sudu + "s linear infinite;";
            // var colorss = "color:" + col[colors] + ";";
            // var spans = "<span" + " style='top:" + rand + "px;" + colorss + dasdass + dasdass2 + "'>" + mes + "</span>";
            //
            // $(".Barrage").append(spans);
        }

    });
    $(".empty").click(function(){
        $(".van-field__control").val("");
        $(".empty").css("opacity","0")
    });
    $(".van-field__control").bind('input propertychange', function() {
        var vals = $(".van-field__control").val();
        if(vals == ""){
            $(".empty").css("opacity","0")
        }else{
            $(".empty").css("opacity","1")
        }
    })

}

function load_header(){
    $.get("../../header.jsp", function (data) {
        $("#header").html(data);
        $("#contests").addClass("active");
        $("body").show();
    });
    $.get("../../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}



$(function () {

    load_header();
    path = $("#path").html();
    cid = location.href.split('=')[1];
    gao();
    setInterval(gao, 1000);
})