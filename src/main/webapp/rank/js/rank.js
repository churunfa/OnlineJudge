function clear(){
    $("#th3 span").hide();
    $("#th4 span").hide();
    $("#th5 span").hide();
    $("#th6 span").hide();
    $("#th3").css("background-color","#FFF");
    $("#th4").css("background-color","#FFF");
    $("#th5").css("background-color","#FFF");
    $("#th6").css("background-color","#FFF");
}
var now;
$(function () {
    $.get("../header.jsp", function (data) {
        $("#header").html(data);
        $("#rank").addClass("active");
        $("body").show();
    });
    $.get("../footer.jsp",function (data) {
        $("#footer").html(data);
    });

    var type = $("#type").html();

    if(type == 1) $("#th3").css("background-color","#CCC"),$("#th3 span").show(),now=3;
    if(type == 2) $("#th4").css("background-color","#CCC"),$("#th4 span").show(),now=4;
    if(type == 3) $("#th5").css("background-color","#CCC"),$("#th5 span").show(),now=5;
    if(type == 4) $("#th6").css("background-color","#CCC"),$("#th6 span").show(),now=6;

    $("#th3").click(function () {
        if(now == 3) return;
        clear();
        now=3;
        // $(this).css("background-color","#CCC");
        // $("#th3 span").show();
        var url = window.location.href;
        var pat = /type=\d+/;
        if (pat.test(url)){
            url = url.replace(pat,"type=1");
            window.location.href=url;
        }else{
            if(/=/.test(url)) url+="&type=1";
            else if(/\?/.test(url))  url+="type=1";
            else url+="?type=1";

            window.location.href=url;
        }
    })
    $("#th4").click(function () {
        if(now==4) return;
        clear();
        now=4;
        // $(this).css("background-color","#CCC");
        // $("#th4 span").show();
        var url = window.location.href;
        var pat = /type=\d+/;
        if (pat.test(url)){
            url = url.replace(pat,"type=2");
            window.location.href=url;
        }else{
            if(/=/.test(url)) url+="&type=2";
            else if(/\?/.test(url))  url+="type=2";
            else url+="?type=2";

            window.location.href=url;
        }
    })
    $("#th5").click(function () {
        if(now==5) return;
        clear();
        now=5;
        // $(this).css("background-color","#CCC");
        // $("#th5 span").show();
        var url = window.location.href;
        var pat = /type=\d+/;
        if (pat.test(url)){
            url = url.replace(pat,"type=3");
            window.location.href=url;
        }else{
            if(/=/.test(url)) url+="&type=3";
            else if(/\?/.test(url))  url+="type=3";
            else url+="?type=3";

            window.location.href=url;
        }
    })
    $("#th6").click(function () {
        if(now==6) return;
        clear();
        now=6;
        // $(this).css("background-color","#CCC");
        // $("#th6 span").show();
        var url = window.location.href;
        var pat = /type=\d+/;
        if (pat.test(url)){
            url = url.replace(pat,"type=4");
            window.location.href=url;
        }else{
            if(/=/.test(url)) url+="&type=4";
            else if(/\?/.test(url))  url+="type=4";
            else url+="?type=4";

            window.location.href=url;
        }
    })
    $("#th3").mouseover(function () {
        $(this).css("background-color","#CCC");
        $("#th3 span").show();
    })
    $("#th3").mouseout(function () {
        if(now==3) return;
        $(this).css("background-color","#FFF");
        $("#th3 span").hide();
    })

    $("#th4").mouseover(function () {
        $(this).css("background-color","#CCC");
        $("#th4 span").show();
    })
    $("#th4").mouseout(function () {
        if(now==4) return;
        $(this).css("background-color","#FFF");
        $("#th4 span").hide();
    })
    $("#th5").mouseover(function () {
        $(this).css("background-color","#CCC");
        $("#th5 span").show();
    })
    $("#th5").mouseout(function () {
        if(now==5) return;
        $(this).css("background-color","#FFF");
        $("#th5 span").hide();
    })
    $("#th6").mouseover(function () {
        $(this).css("background-color","#CCC");
        $("#th6 span").show();
    })
    $("#th6").mouseout(function () {
        if(now==6) return;
        $(this).css("background-color","#FFF");
        $("#th6 span").hide();
    })
    $("#page_btn").click(function () {
        var mx = $("#maxPg").html();
        var newPg = $("#page_key").val()
        if(newPg.length==0) return;
        if(parseInt(newPg)>parseInt(mx)){
            alert("没有这么多页了。。。");
            return
        }
        if(parseInt(newPg)<=0){
            alert("页码最小为1");
            return;
        }
        var url = window.location.href;
        var pat = /pg=\d+/;
        if (pat.test(url)){
            url = url.replace(pat,"pg="+newPg);
            window.location.href=url;
        }else{
            if(/=/.test(url)) url+="&pg="+newPg;
            else if(/\?/.test(url))  url+="pg="+newPg;
            else url+="?pg="+newPg;

            window.location.href=url;
        }
    })
})