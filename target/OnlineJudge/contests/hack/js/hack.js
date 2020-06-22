/*
// or
testEditor = editormd({
    id      : "test-editormd",
    width   : "90%",
    height  : 640,
    path    : "../lib/"
});
*/
var data_now=1,tag_now=1;
var cid,pid,path;
function load_header() {
    $.get("../../header.jsp", function (data) {
        $("#header").html(data);
        $("#contests").addClass("active");
        $("body").show();
    });
    $.get("../../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}

function getElem(){
    var data_elem='        <div id="simple-data'+data_now+'" class="row data">\n' +
        '            <div class="col-md-5">\n' +
        '                <textarea type="text" rows="6" class="form-control data-in" placeholder="输入"></textarea>\n' +
        '            </div>\n' +
        '            <div class="col-md-5">\n' +
        '                <textarea type="text" rows="6" class="form-control data-out" placeholder="输出"></textarea>\n' +
        '            </div>\n' +
        '            <div class="col-md-2" style="text-align: left">\n' +
        '                <button class="btn btn-default glyphicon glyphicon-remove"style="font-size: 8px;text-align: left" onclick="del('+data_now+')"></button>\n' +
        '            </div>\n' +
        '        </div>';
    return data_elem
}

function del(u) {
    var s="#simple-data"+u;
    $(s).remove();

}

function add_data(){
    $("#simple-data").append(getElem());
    data_now++;
}

function del_tag(u){
    var s= "#tag"+u;
    $(s).remove();
}
function submit_hack(){
    var form = new FormData();

    form.append("pid",pid);

    var data_in = new Array();
    var data_out = new Array();

    $(".data-in").each(function () {
        var data = $(this).val();
        data_in.push(data);
    });

    $(".data-out").each(function () {
        var data = $(this).val();
        data_out.push(data);
    });

    for(var i = 0;i<data_in.length;i++) if(data_in[i].length == 0) data_in[i] = " ";
    for(var i = 0;i<data_out.length;i++) if(data_out[i].length == 0) data_out[i] = " ";

    var data_in_str = "";
    var data_out_str = "";
    if(data_in.length>=1) data_in_str = data_in[0];
    if(data_out.length>=1) data_out_str = data_out[0];
    for(var i=1;i<data_in.length;i++) data_in_str = data_in_str + ",]|[," + data_in[i];
    for(var i=1;i<data_out.length;i++) data_out_str = data_out_str + ",]|[," + data_out[i];

    if(data_in.length >= 1) form.append("data_in",data_in_str);
    if(data_out.length >= 1) form.append("data_out",data_out_str);

    // var datafile = $("#dataFile")[0].files[0];
    // if(datafile != null){
    //     form.append("datafile",datafile);
    // }

    var tags = new Array();
    $(".tag-text").each(function () {
        var tag_text = $(this).html();
        tags.push(tag_text);
    });

    form.append("tags",tags);

    $.ajax({
        url: path + "/problemServlet/addHackServlet",
        type: "post",
        contentType: "multipart/form-data",
        data:form,

        processData: false, //很重要，告诉jquery不要对form进行处理
        contentType: false, //很重要，指定为false才能形成正确的Content-Type
        success: function(data) {
            alert(data.msg);
            location.reload();
            $("#submit").button('reset');
        },
        error: function() {
            alert("连接网络错误，请稍后再试");
            $("#submit").button('reset');
        }
    });


}
$(function () {
    load_header();
    $("#add_data").click(add_data)
    $("#tag-btn").click(function () {
        var s=$("#add_tag").val();
        if(s.length == 0){
            alert("不能添加空标签");
            return;
        }
        var tag_elem='<a class="tag-text" id="tag'+tag_now+'" onclick="del_tag('+tag_now+')">'+s+'</a>';
        $("#tags").append(tag_elem);

        tag_now++;
    });
    $("#tags a").mouseover(function () {
        $(this).addClass("glyphicon glyphicon-remove");
    })
    $("#tags a").mouseout(function () {
        $(this).removeClass("glyphicon glyphicon-remove");
    })
    $("#code_template_switch_close").click(function () {
        $(this).hide();
        $("#code_template_switch_open").show();
        $("#code_template_pre").show();
        code_template_flag=true;
    })
    $("#code_template_switch_open").click(function () {
        $(this).hide();
        $("#code_template_switch_close").show();
        $("#code_template_pre").hide();
        code_template_flag=false;
    });
    $("#code_spj_switch_close").click(function () {
        $(this).hide();
        $("#code_spj_switch_open").show();
        $("#code_spj_pre").show();
        spj_flag=true;
    });
    $("#code_spj_switch_open").click(function () {
        $(this).hide();
        $("#code_spj_switch_close").show();
        $("#code_spj_pre").hide();
        spj_flag=false;
    });
    $("#code_interactive_switch_close").click(function () {
        $(this).hide();
        $("#code_interactive_switch_open").show();
        $("#code_interactive_pre").show();
        interactive_flag=true;
    });
    $("#code_interactive_switch_open").click(function () {
        $(this).hide();
        $("#code_interactive_switch_close").show();
        $("#code_interactive_pre").hide();
        interactive_flag=false;
    });

    pid = $("#pid").html();
    path = $("#path").html();

    $("#submit").click(function(){
        var $btn = $(this).button('loading');
        submit_hack();
    });

})

