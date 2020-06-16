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
$(function () {
    load_header();

})