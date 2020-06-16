$(function () {
    $.get("../../header.jsp", function (data) {
        $("#header").html(data);
        $("#contests").addClass("active");
        $("body").show();
    });
    $.get("../../footer.jsp",function (data) {
        $("#footer").html(data);
    });
    var id = $("#id").html();
    path = $("#path").html();
    $("#sign").click(function () {
        var lv = $("#lv").html();
        var div = $("#div");
        if(lv == -1){
            alert("请登录之后再进行报名");
            return;
        }
        if(div == "div3" && lv >= 3){
            alert("div3以下用户才可以报名此比赛");
            return;
        }
        if(div == "div2" && lv >=4 ){
            alert("div4以下用户才可以报名此比赛");
            return;
        }

        $.ajax( {
            url:path+"/contestServlet/signServlet",// 跳转到
            data:{
                id : id,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    // $("#sign").attr("href",path + "/contests?id="+id);
                    window.location.href = path + "/contests";
                }else{
                    alert(data.msg);
                }
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
            }
        });
    })
})