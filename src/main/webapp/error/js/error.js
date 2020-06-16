var path;
function load_header() {
    path = $("#path").html();
    $.get(path+"/header.jsp", function (data) {
        $("#header1").html(data);
        $("body").show();
    });
    $.get(path+"/footer.jsp",function (data) {
        $("#footer").html(data);
    });
}
$(function () {
    load_header();
})