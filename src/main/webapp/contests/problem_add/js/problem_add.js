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
var code_template_flag=false;
var spj_flag=false;
var interactive_flag=false;
var cid,pid,path;

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
function load_editor(id,msg){
    testEditor = editormd(id, {
        placeholder:msg,  //默认显示的文字，这里就不解释了
        width: "90%",
        height: 640,
        syncScrolling: "single",
        path: "../../js/editor.md-master/lib/",   //你的path路径（原资源文件中lib包在我们项目中所放的位置）
        // theme: "dark",//工具栏主题
        // previewTheme: "dark",//预览主题
        // editorTheme: "pastel-on-dark",//编辑主题
        saveHTMLToTextarea: true,
        emoji: true,
        taskList: true,
        tocm: true,         // Using [TOCM]
        tex: true,                   // 开启科学公式TeX语言支持，默认关闭
        flowChart: true,             // 开启流程图支持，默认关闭
        sequenceDiagram : true,       // 开启时序/序列图支持，默认关闭,
        // dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为true
        // dialogShowMask : false,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为true
        // dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为true
        dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为0.1
        dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为#fff
        imageUpload: true,
        imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL: '../../contestServlet/imgServlet', //图片上传后台地址
        onload: function() {
            // 引入插件 执行监听方法
            editormd.loadPlugin("../../js/editor.md-master/plugins/image-handle-paste/image-handle-paste", function(){
                testEditor.imagePaste();
            });
        },
        toolbarIcons : function() {  //自定义工具栏，后面有详细介绍
            // return editormd.toolbarModes['simple']; // full, simple, mini
            my = [
                "undo", "redo", "|",
                "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
                "h1", "h2", "h3", "h4", "h5", "h6", "|",
                "list-ul", "list-ol", "hr", "|",
                "link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime","emoji", "html-entities", "pagebreak", "|",
                "goto-line", "watch", "preview", "clear", "search", "|",
                "help"
            ];
            return  my;
        },
    });

//上面的挑有用的写上去就行
//     testEditor = editormd("test-editormd", {
    //     width   : "90%",
    //     height  : 640,
    //     syncScrolling : "single",
    //     path    : "/js/editor.md-master/lib/"
    // });

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

function creat_problem() {
    if(cid==null){
        location.href = path + "/error";
        return;
    }
    var show_id = $("#show_id").val();
    var title = $("#title").val();
    var time_limit = $("#time_limit").val();
    var memory_limit = $("#memory_limit").val();
    if(show_id.length == 0){
        alert("题目展示ID不能为空");
        $("#submit").button('reset');
        return;
    }
    if(title.length == 0){
        alert("题目不能为空");
        $("#submit").button('reset');
        return;
    }
    if(time_limit.length == 0){
        alert("时间限制不能为空");
        $("#submit").button('reset');
        return;
    }
    if(memory_limit.length == 0){
        alert("空间限制不能为空");
        $("#submit").button('reset');
        return;
    }
    $.ajax( {
        url:path+"/problemServlet/creatServlet",// 跳转到
        data:{
            cid : cid,
            show_id : show_id,
            title:title,
            time_limit : time_limit,
            memory_limit:memory_limit,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                pid = data.key;
                submit_problem();
            }else{
                alert(data.msg);
                $("#submit").button('reset');
            }
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
        }
    });
}

function submit_problem(){
    var form = new FormData();

    var show_id = $("#show_id").val();
    var title = $("#title").val();
    var pro_text = $("#pro_text").html();
    var languages = new Array();
    var time_limit = $("#time_limit").val();
    var memory_limit = $("#memory_limit").val();
    $(".checkbox-inline input:checked").each(function () {
        languages.push($(this).val());
    });

    if(show_id.length == 0){
        alert("题目展示ID不能为空");
        $("#submit").button('reset');
        return;
    }
    if(title.length == 0){
        alert("题目不能为空");
        $("#submit").button('reset');
        return;
    }
    if(time_limit.length == 0){
        alert("时间限制不能为空");
        $("#submit").button('reset');
        return;
    }

    if(memory_limit.length == 0){
        alert("空间限制不能为空");
        $("#submit").button('reset');
        return;
    }

    for(var i = 0;i<time_limit;i++){
        if(time_limit[i] < '0' ||time_limit[i]>'9'){
            alert("时间限制必须为正整数");
            $("#submit").button('reset');
            return;
        }
    }

    var sum = 0;
    for(var i=0;i<memory_limit;i++){
        if(memory_limit[i]=='.'){sum++;continue;}
        if(memory_limit[i] < '0' ||memory_limit[i]>'9'){
            alert("请输入正确的空间限制");
            $("#submit").button('reset');
            return;
        }
    }

    if(sum >1){
        alert("请输入正确的空间限制");
        $("#submit").button('reset');
        return;
    }

    form.append("cid",cid);
    form.append("pid",pid);
    form.append("show_id",show_id);
    form.append("title",title);
    form.append("time_limit",time_limit);
    form.append("memory",memory_limit);
    form.append("pro_text",pro_text);
    form.append("languages",languages);

    var code_temp = $("#code_template").val();
    if(!code_template_flag) code_temp = "";
    var spj = $("#code_spj").val();
    if(!spj_flag) spj = "";
    var interactive = $("#code_interactive").val();
    if(!interactive_flag) interactive = "";

    form.append("code_temp",code_temp);
    form.append("spj",spj);
    form.append("interactive",interactive);

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

    var datafile = $("#dataFile")[0].files[0];
    if(datafile != null){
        form.append("datafile",datafile);
    }

    var tags = new Array();
    $(".tag-text").each(function () {
       var tag_text = $(this).html();
       tags.push(tag_text);
    });

    form.append("tags",tags);

    $.ajax({
        url: path + "/problemServlet/updateServlet",
        type: "post",
        contentType: "multipart/form-data",
        data:form,

        processData: false, //很重要，告诉jquery不要对form进行处理
        contentType: false, //很重要，指定为false才能形成正确的Content-Type
        success: function(data) {
            if(data.success){
                $("#submit").button('reset');
                alert(data.msg);
                location.href = path + "/contests/problem_add/?pid=" + pid;
            }else{
                alert(data.msg);
                $("#submit").button('reset');
            }
        },
        error: function() {
            alert("连接网络错误，请稍后再试");
            $("#submit").button('reset');
        }
    });


}

$(function () {
    load_header();
    load_editor("desc",'');
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
    cid = $("#cid").html();
    path = $("#path").html();
    var sum = $("#data_sum").html();
    if(sum != null && sum.length > 0) data_now = parseInt(sum)+1;

    var tag_sum = $("#tag_sum").html();
    if(tag_sum != null && tag_sum.length > 0) tag_now = parseInt(tag_sum)+1;

    var code_template = $("#code_template").val();
    if(code_template.length>0){
        $("#code_template_switch_close").hide();
        $("#code_template_switch_open").show();
        $("#code_template_pre").show();
        code_template_flag=true;
    }

    var code_spj = $("#code_spj").val();
    if(code_spj.length>0){
        $("#code_spj_switch_close").hide();
        $("#code_spj_switch_open").show();
        $("#code_spj_pre").show();
        spj_flag=true;
    }

    var interactive = $("#code_interactive").val();
    if(interactive.length>0){
        $("#code_interactive_switch_close").hide();
        $("#code_interactive_switch_open").show();
        $("#code_interactive_pre").show();
        interactive_flag=true;
    }

    $("#submit").click(function(){
        var $btn = $(this).button('loading');
        if(pid.length == 0) creat_problem();
        else submit_problem();
    });

})

