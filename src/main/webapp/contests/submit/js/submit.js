var path;
function change_language(){
    var pid = $("#problem_select option:selected").val();
    $.ajax( {
        url:path+"/submitServlet/getLanguage",// 跳转到
        data:{
            pid:pid,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                var lan = "";
                for(var i=0;i<data.data.length;i++){
                    if(i==0) lan = lan +"<option selected>"+data.data[i]+"</option>\n"
                    else lan = lan +"<option>"+data.data[i]+"</option>\n"
                }
                $("#languages").html(lan);
            }
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
        }
    });
}
$(function () {
    $.get("../../header.jsp", function (data) {
        $("#header").html(data);
        $("#contests").addClass("active");
        $("body").show();
    });
    $.get("../../footer.jsp",function (data) {
        $("#footer").html(data);
    });
    path = $("#path").html();
    $('#btn').on('click', function () {
        var $btn = $(this).button('loading');
        var language = $("#languages option:selected").html();
        var pid = $("#problem_select option:selected").val();
        var code = $("#code").val();
        /**
         * language
         * pid
         * code
         */
        $.ajax( {
            url:path+"/submitServlet/submitCode",// 跳转到
            data:{
                language:language,
                pid:pid,
                code:code,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    alert("提交成功");
                    location.href = path + "/contests/status/?pid="+pid;
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
        // $btn.button('reset')
    })
})