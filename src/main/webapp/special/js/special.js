function init_start(){
    jeDate("#start",{
        theme:{ bgcolor:"#00A1CB",color:"#ffffff", pnColor:"#00CCFF"},
        festival:true,
        minDate:"1900-01-01",              //最小日期
        maxDate:"2099-12-31",              //最大日期
        method:{
            choose:function (params) {
            }
        },
        format: "YYYY-MM-DD hh:mm:ss",
        donefun: function(obj){
            //this    而this指向的都是当前实例
            console.log(obj.elem);     //得到当前输入框的ID
            console.log(obj.val);      //得到日期生成的值，如：2017-06-16
            //得到日期时间对象，range为false
            console.log(obj.date);    //这里是对象
        }
    });
}
function init_end(){
    jeDate("#end",{
        theme:{ bgcolor:"#00A1CB",color:"#ffffff", pnColor:"#00CCFF"},
        festival:true,
        minDate:"1900-01-01",              //最小日期
        maxDate:"2099-12-31",              //最大日期
        method:{
            choose:function (params) {
            }
        },
        format: "YYYY-MM-DD hh:mm:ss",
        donefun: function(obj){
            //this    而this指向的都是当前实例
            console.log(obj.elem);     //得到当前输入框的ID
            // alert(obj.elem.id);
            console.log(obj.val);      //得到日期生成的值，如：2017-06-16
            //得到日期时间对象，range为false
            console.log(obj.date);    //这里是对象
        }
    });
}
function load_header(){
    $.get("../header.jsp", function (data) {
        $("#header").html(data);
        $("#special").addClass("active");
        $("body").show();
    });
    $.get("../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}

$(function () {
    load_header();
    init_start();
    init_end();
})