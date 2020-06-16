$(function () {
    $.get("../header.jsp", function (data) {
        $("#header").html(data);
        $("#CF").addClass("active");
        $("body").show();
    });
    $.get("../footer.jsp",function (data) {
        $("#footer").html(data);
    });
})