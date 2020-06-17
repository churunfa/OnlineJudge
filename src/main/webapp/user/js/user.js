function load_header(){
    $.get("../header.jsp", function (data) {
        $("#header").html(data);
        $("body").show();
    });
    $.get("../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}
function init_echarts(contest_data,ranting_data){

    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('rating'));

    // 指定图表的配置项和数据
    var option = {
        title: {
            text: '评分变化'
        },
        tooltip: {},
        legend: {
            data:['ranting']
        },
        xAxis: {
            // data: ["#1","#2","#3","#4","#5","#6","#7"]
            data:contest_data
        },
        yAxis: {},
        series: [{
            name: '评分',
            type: 'bar',
            // data: [5, 100, 1000, 1500, 1400, 1600,1500]
            data:ranting_data,
        }]
    };
    myChart.setOption(option);
}

var visit_id,path;

$(function () {
    load_header();
    visit_id = $("#visit_id").html();
    $(".img_div_self").click(function () {
        $("#load_img").trigger("click");
    })
    $("#load_img").change(function () {
        var file = $("#load_img")[0].files[0];
        var form = new FormData();
        form.append("file", file);
        form.append("id",visit_id)
        $.ajax({
            url: "../userServlet/imgServlet",
            type: "post",
            contentType: "multipart/form-data",
            data:form,

            processData: false, //很重要，告诉jquery不要对form进行处理
            contentType: false, //很重要，指定为false才能形成正确的Content-Type
            success: function(img) {
                if(img.success){
                    $(".head").attr("src",path + img.url);
                    $("#head_img").attr("src",path + img.url);
                }else{
                    alert(img.message);
                }
            },
            error: function() {
                alert("连接网络错误，请稍后再试");
            }
        });
    });
    $("#submit_user_info").click(function () {

        var name = $("#inputName").val();
        var uid = $("#inputUid").val();
        var major = $("#inputMajor").val();
        var grade = $("#inputGrade").val();
        var sex = $('#sex option:selected').val();
        if(name.length == 0 ){
            alert("昵称不能为空");
            return;
        }
        if(name.length>15){
            alert("昵称过长");
        }
        if(uid.length == 0){
            alert("学号不能为空");
            return;
        }
        if(uid.length>15){
            alert("学号不合法");
            return;
        }
        var myreg=/^[A-Za-z0-9]+$/;
        if (!myreg.test(uid)){
            alert("学号不合法")
            return;
        }
        myreg =  /^[ ]+$/;
        if(myreg.test(name)){
            alert("昵称不能为空");
            return;
        }
        var flag=true;
        for(var i=0;i<name.length;i++){
            if(name[i]=='$'||name[i]=='<'||name[i]=='>'||name[i]=='@'||name[i]=='#') flag=false;
        }
        if(!flag){
            alert("昵称中不能包含特殊符号");
            return;
        }
        $.ajax( {
            url:path+"/userServlet/updateServlet",// 跳转到
            data:{
                name : name,
                uid : uid,
                major:major,
                grade : grade,
                sex:sex,
                visit_id:visit_id,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    location.reload()
                }else{
                    alert(data.msg)
                }
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
            }
        });
    })
    $("#send_email").click(function () {
        var code = $("#email_code").val();
        $.ajax( {
            url:path+"/userServlet/emailSendServlet",// 跳转到
            data:{
                visit_id:visit_id,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                }else{
                    alert(data.msg)
                }
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
            }
        });
    })
    $("#sub_btn").click(function () {
        var code = $("#email_code").val();
        $.ajax( {
            url:path+"/userServlet/emailActiveServlet",// 跳转到
            data:{
                code:code,
                visit_id:visit_id,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    location.reload()
                }else{
                    $("#email_msg").show();
                }
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
            }
        });
    })
    path = $("#prePath").html();
    $.ajax( {
        url:path+"/userServlet/rantingChangeServlet",// 跳转到
        data:{
            id:visit_id
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                init_echarts(data.contest,data.ranting);
            }else{
            }
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
        }
    });
})