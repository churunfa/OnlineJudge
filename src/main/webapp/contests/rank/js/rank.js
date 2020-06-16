var path,cid;
var st_time,ed_time,now_time,r,tt;
var askCount = 0;
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
function get_time(){
    $.ajax( {
        url:path+"/contestServlet/askNowTime",// 跳转到
        data:{
            cid:cid,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            console.log(data);
            if(data.success){
                askCount++;
                st_time = data.data.st;
                ed_time = data.data.ed;
                now_time = data.data.now;
                r = data.data.r;
                $("#progress").css("width",r+"%");
                $("#progress span").html(r+"%");
                if(r == 100){
                    if(askCount > 1) alert("比赛结束");
                    clearInterval(tt);
                }
            }else{
                console.log("获取时间失败");
            }
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
        }
    });
}
$(function () {
    path = $("#path").html();
    cid = $("#cid").html();
    load_header();
    tt = setInterval(get_time, 1000);
})