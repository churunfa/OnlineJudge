function init_text() {
    var textarea = document.getElementById('comment');
    function makeExpandingArea(el){
        var setStyle = function(el){
            el.style.height = 'auto';
            el.style.height = el.scrollHeight + 'px';
            // console.log(el.scrollHeight);
        }
        var delayedResize = function(el) {
            window.setTimeout(function(){
                setStyle(el)
            }, 0);
        }
        if(el.addEventListener){
            el.addEventListener('input',function(){
                setStyle(el)
            },false);
            setStyle(el)
        }else if(el.attachEvent){
            el.attachEvent('onpropertychange',function(){
                setStyle(el)
            })
            setStyle(el)
        }
        if(window.VBArray && window.addEventListener) { //IE9
            el.attachEvent("onkeydown", function() {
                var key = window.event.keyCode;
                if(key == 8 || key == 46) delayedResize(el);

            });
            el.attachEvent("oncut", function(){
                delayedResize(el);
            });//处理粘贴
        }
    }
    makeExpandingArea(textarea);
}
function load_header(){
    $.get("../header.jsp", function (data) {
        $("#header").html(data);
        $("body").show();
    });
    $.get("../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}
$(function () {
    load_header();
    init_text();
    $("#submit").click(function () {
        var text = $("#comment").val();
        if(text.length == 0) return;
        $.ajax( {
            url:path+"/userServlet/BugServlet",// 跳转到
            data:{
                text:text,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    location.reload();
                }else{
                    alert("提交失败")
                }
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
            }
        });
    })
})