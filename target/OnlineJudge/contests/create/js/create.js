var st,ed,cid;

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
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

function init_start(){
    jeDate("#start",{
        theme:{ bgcolor:"#00A1CB",color:"#ffffff", pnColor:"#00CCFF"},
        festival:true,
        minDate:"1900-01-01",              //最小日期
        maxDate:"2099-12-31",              //最大日期
        method:{
            choose:function (params) {
            }
        },
        format: "YYYY-MM-DD hh:mm:ss",
        donefun: function(obj){
            st = new Date(obj.val);
            //this    而this指向的都是当前实例
            // console.log(obj.elem);     //得到当前输入框的ID
            // console.log(obj.val);      //得到日期生成的值，如：2017-06-16
            //得到日期时间对象，range为false
            // console.log(obj.date);    //这里是对象
        }
    });
}
function init_end(){
    jeDate("#end",{
        theme:{ bgcolor:"#00A1CB",color:"#ffffff", pnColor:"#00CCFF"},
        festival:true,
        minDate:"1900-01-01",              //最小日期
        maxDate:"2099-12-31",              //最大日期
        method:{
            choose:function (params) {
            }
        },
        format: "YYYY-MM-DD hh:mm:ss",
        donefun: function(obj){
            ed = new Date(obj.val);
            //this    而this指向的都是当前实例
            // console.log(obj.elem);     //得到当前输入框的ID
            // // alert(obj.elem.id);
            // console.log(obj.val);      //得到日期生成的值，如：2017-06-16
            // //得到日期时间对象，range为false
            // console.log(obj.date);    //这里是对象
        }
    });
}

$(function () {
    load_header();
    init_start();
    init_end();

    path = $("#path").html();
    cid = location.href.split('=')[1];

    if(cid != null){
        $.ajax( {
            url:path+"/contestServlet/findContestServlet",// 跳转到
            data:{
                cid:cid
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    $("#title").val(data.data.name);
                    $("#desc").val(data.data.notice);

                    st = new Date(data.data.start_time);
                    ed = new Date(data.data.end_time);

                        $("#start").val(st.format("yyyy-MM-dd hh:mm:ss"));
                    $("#end").val(ed.format("yyyy-MM-dd hh:mm:ss"));

                    var html = $("[value="+data.data.type+"]");
                    html.attr("selected","selected");
                }else{
                    location.href  = path + "/error";
                }
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
            }
        });
    }

    $('#submit').on('click', function () {
        var $btn = $(this).button('loading')
        var title = $("#title").val();
        var type = $("#type option:selected").val();
        var msg = $("#desc").val();

        if(st == null){
            alert("请选择开始时间");
            $btn.button('reset')
            return;
        }
        if(ed == null){
            $btn.button('reset')
            alert("请选择结束时间");
            return;
        }

        if(type.length==0){
            $btn.button('reset')
            alert("比赛类型不能为空");
            return;
        }

        if(st.getTime()>ed.getTime()){
            alert("结束时间不能早于开始时间");
            $btn.button('reset')
            return;
        }
        
        var flag = true;
        for(var i=0;i<title.length;i++)
            if(title[i]=='<'||title[i]=='>'||title[i]=='#'&&title[i]=='$') flag = false;
            
        if(!flag){
            alert("标题中不能包含特殊符号");
            $btn.button('reset')
            return;
        }
        $.ajax( {
            url:path+"/contestServlet/updateServlet",// 跳转到
            data:{
                cid:cid,
                title:title,
                type:type,
                msg:msg,
                st:st.getTime(),
                ed:ed.getTime(),
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    alert("提交成功");
                    location.href = path + "/contests";
                }else{
                    alert(data.msg);
                }
                $btn.button('reset')
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
                $btn.button('reset')
            }
        });
    })
})