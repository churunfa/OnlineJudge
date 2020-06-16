function load_header(){
    $.get("../header.jsp", function (data) {
        $("#header").html(data);
        $("#contests").addClass("active");
        $("body").show();
    });
    $.get("../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}

var t;

function clo() {
    $("#success").hide();
    clearTimeout(t);
}

function fsign(id){
    $("#success").show();
    t = setTimeout("clo();", 3000);
}

$(function () {
    load_header();
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