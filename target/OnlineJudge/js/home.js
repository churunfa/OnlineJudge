$(function () {
    $.get("./header.jsp", function (data) {
        $("#header").html(data);
        $("#home").addClass("active");
        $("body").show();
    });
    $.get("./footer.jsp",function (data) {
        $("#footer").html(data);
    });
})