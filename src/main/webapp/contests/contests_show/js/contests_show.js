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
function init_text() {
    var textarea = document.getElementById('news');
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
function compile(text){

    //创建实例
    var converter = new showdown.Converter();
    //进行转换
    var html = converter.makeHtml(text);
    //展示到对应的地方  result便是id名称
    // document.getElementById("result").innerHTML = html;
    return html;
}

function gao(flag=true){
    $.ajax( {
        url:path+"/contestServlet/askNotice",// 跳转到
        data:{
            cid:cid,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                var html = compile(data.msg);
                if(!flag){
                    $("#tz").html(html);
                    MathJax.Hub.Typeset(document.getElementById("tz"));
                    $("#news").val(data.msg);
                    init_text();
                    $("#old_tz").val(data.msg);
                } else{
                    var old_html = $("#old_tz").val();
                    if(old_html != data.msg){
                        $("#tongzhi").html(html);
                        MathJax.Hub.Typeset(document.getElementById("tongzhi"));
                        $("#tz").html(html);
                        MathJax.Hub.Typeset(document.getElementById("tz"));
                        $("#old_tz").val(data.msg);
                        $("#tz_btn").trigger("click");
                        $("#news").val(data.msg);
                        init_text();
                    }
                }

            }else{
                console.log(data);
            }
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
        }
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
            console.log(data)
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
    cid = location.href.split("=")[1];
    load_header();
    gao(false);
    init_text();
    setInterval(gao, 1000);
    $("#update").click(function () {
        var msg = $("#news").val();
        $.ajax( {
            url:path+"/contestServlet/updateNotice",// 跳转到
            data:{
                cid:cid,
                msg:msg,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    gao(false);
                    alert("更新成功!");
                }else{
                    console.log(data);
                }
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
            }
        });
    });
    tt = setInterval(get_time, 1000);

})