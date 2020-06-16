$(function () {
    function Now(){
        var curPageUrl = window.document.location.href;
        var Now = curPageUrl.split("//")[1].split("/")[2];
        return Now;
    }

    $(function () {
        $.get("/OnlineJudge/header.jsp", function (data) {
            $("#header").html(data);
            $("body").show();
            var s = "#";
            var now = Now();
            if (now == null || now == "") s += "home";
            else s += now;
            if ($(s)) $(s).addClass("active");
        });
        $.get("/OnlineJudge/footer.jsp",function (data) {
            $("#footer").html(data);
        });
    })

})



