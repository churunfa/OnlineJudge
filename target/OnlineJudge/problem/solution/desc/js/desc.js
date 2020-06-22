function load_header(){
    $.get("../../../header.jsp", function (data) {
        $("#header").html(data);
        $("#problem").addClass("active");
        $("body").show();
    });
    $.get("../../../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}
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
function init_text2() {
    var textareas = document.getElementsByClassName('reply');
    for(var i=0;i<textareas.length;i++){
        textarea=textareas[i];
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
}

function compile(text){

    //创建实例
    var converter = new showdown.Converter();
    //进行转换
    var html = converter.makeHtml(text);
    //展示到对应的地方  result便是id名称
    // document.getElementById("result").innerHTML = html;
    return html;
}

$(function () {
    load_header();
    init_text();
    init_text2();
    var id = $("#id").html();
    var path = $("#path").html();
    $.ajax( {
        url:path+"/blogServlet/showServlet",// 跳转到
        data:{
            id:id,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                var html = compile(data.msg);
                console.log(data);
                $("#sol-desc").html(html);
            }else{

            }
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
        }
    });
    $(".heart").click(function () {
        $.ajax( {
            url:path+"/blogServlet/changeLoveServlet",// 跳转到
            data:{
                bid:id,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    if(data.key){
                        // glyphicon-heart-empty
                        $(".hear").removeClass("glyphicon-heart-empty");
                        $(".hear").addClass("glyphicon-heart");
                        $(".love_sum").html(data.msg)
                    }else{
                        $(".hear").removeClass("glyphicon-heart");
                        $(".hear").addClass("glyphicon-heart-empty");
                        $(".love_sum").html(data.msg)
                    }
                }else{

                }
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
            }
        });
    })
})