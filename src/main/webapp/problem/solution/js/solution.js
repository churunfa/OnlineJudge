var path;
function load_header(){
    $.get("../../header.jsp", function (data) {
        $("#header").html(data);
        $("#problem").addClass("active");
        $("body").show();
    });
    $.get("../../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}

$(function () {
    load_header();
    $(".del").mouseover(function () {
        $(this).html("您确认要删除么？")
        $(this).css("width","150px");
    })
    $(".del").mouseout(function () {
        $(this).html("删除")
        $(this).css("width","70px");
    })
})
