var path,pid;
function init_editor(){
    //初始化对象
    editor = ace.edit("code");

    //设置风格和语言（更多风格和语言，请到github上相应目录查看）
    theme = "clouds"
    language = "c_cpp"
    editor.setTheme("ace/theme/" + theme);
    editor.session.setMode("ace/mode/" + language);

    editor.setShowPrintMargin(false);

    //字体大小
    editor.setFontSize(18);

    //设置只读（true时只读，用于展示代码）
    editor.setReadOnly(false);

    //自动换行,设置为off关闭
    editor.setOption("wrap", "free")

    //启用提示菜单
    ace.require("ace/ext/language_tools");
    editor.setOptions({
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true
    });

    var data=[
        {meta: "keyword", caption: "include", value: "include", score:1},
        {meta: "keyword", caption: "iostream", value: "iostream", score:1},
        {meta: "keyword", caption: "algorithm", value: "algorithm", score:1},
        {meta: "keyword", caption: "cstdio", value: "cstdio", score:1},
        {meta: "keyword", caption: "stdio.h", value: "stdio.h", score:1},
        {meta: "keyword", caption: "cstring", value: "cstring", score:1},
        {meta: "keyword", caption: "string.h", value: "string.h", score:1},
        {meta: "keyword", caption: "bits/stdc++.h", value: "bits/stdc++.h", score:1},
        {meta: "keyword", caption: "map", value: "map", score:1},
        {meta: "keyword", caption: "std", value: "std", score:1},
        {meta: "keyword", caption: "define", value: "define", score:1},
        {meta: "keyword", caption: "priority_queue", value: "priority_queue", score:1},
        {meta: "keyword", caption: "greater", value: "greater", score:1},
        {meta: "keyword", caption: "unordered_map", value: "unordered_map", score:1},
        {meta: "keyword", caption: "cstdlib", value: "cstdlib", score:1},
        {meta: "keyword", caption: "stdlib.h", value: "stdlib.h", score:1},
        {meta: "keyword", caption: "main", value: "main", score:1},
        {meta: "keyword", caption: "input", value: "input", score:1},
        {meta: "keyword", caption: "split", value: "split", score:1},
        {meta: "keyword", caption: "scanf", value: "scanf", score:1},

    ];
    var setCompleteData = function(data) {
        var langTools = ace.require("ace/ext/language_tools");
        langTools.addCompleter({
            getCompletions: function(editor, session, pos, prefix, callback) {
                if (prefix.length === 0) {
                    return callback(null, []);
                } else {
                    return callback(null, data);
                }
            }
        });
    }
    setCompleteData(data);
}

function init_text() {
    var textarea = document.getElementById('run-code-stdin');
    function makeExpandingArea(el){
        var setStyle = function(el){
            el.style.height = 'auto';
            el.style.height = el.scrollHeight + 'px';
            // console.log(el.scrollHeight);
        }
        var delayedResize = function(el) {
            window.setTimeout(function(){
                setStyle(el)
            }, 0);
        }
        if(el.addEventListener){
            el.addEventListener('input',function(){
                setStyle(el)
            },false);
            setStyle(el)
        }else if(el.attachEvent){
            el.attachEvent('onpropertychange',function(){
                setStyle(el)
            })
            setStyle(el)
        }
        if(window.VBArray && window.addEventListener) { //IE9
            el.attachEvent("onkeydown", function() {
                var key = window.event.keyCode;
                if(key == 8 || key == 46) delayedResize(el);

            });
            el.attachEvent("oncut", function(){
                delayedResize(el);
            });//处理粘贴
        }
    }
    makeExpandingArea(textarea);
}
function load_header() {
    $.get("../../header.jsp", function (data) {
        $("#header").html(data);
        $("#problem").addClass("active");
        $("body").show();
    });
    $.get("../../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}
function compile(text){

    //创建实例
    var converter = new showdown.Converter();
    //进行转换
    var html = converter.makeHtml(text);
    //展示到对应的地方  result便是id名称
    // document.getElementById("result").innerHTML = html;
    return html;
}
function save(){
    var pid = $("#pid").html();
    var code = editor.getValue();

    if(code.length == 0) return;

    //pid
    //code
    $.ajax( {
        url:path+"/problemServlet/updateCodeServlet",// 跳转到
        data:{
            pid:pid,
            code:code,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
        }
    });
}

function show_out() {
    $.ajax( {
        url:path+"/submitServlet/askOutServlet",// 跳转到
        data:{
            solution_id:solution_id,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                $("#run-code-stdout").html(data.msg);
            }else{
                alert("获取结果失败");
            }
            $("#run").button('reset');
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
            $("#run").button('reset')
        }
    });

}

function askRes(){
    $.ajax( {
        url:path+"/submitServlet/getResultServlet",// 跳转到
        data:{
            solution_id:solution_id,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                clearInterval(ide_tt);
                $("#sta").html(data.msg);
                show_out();
            }
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
            $("#run").button('reset')
        }
    });
}
$(function () {
    path = $("#path").html();
    load_header();
    init_editor();
    init_text();
    var html = compile($("#text").html());
    $("#pro_main").html(html);
    setInterval(save,3000);
    $('#sub').on('click', function () {
        var $btn = $(this).button('loading');
        var language = $("#languages option:selected").html();
        var pid = $("#pid").html();
        var code = editor.getValue();
        /**
         * language
         * pid
         * code
         */
        $.ajax( {
            url:path+"/submitServlet/submitCode",// 跳转到
            data:{
                language:language,
                pid:pid,
                code:code,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    save();
                    alert("提交成功");
                    location.href = path + "/status";
                }else{
                    alert(data.msg);
                }
                $btn.button('reset')
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
                $btn.button('reset')
            }
        });
        // $btn.button('reset')
    });
    $("#run").on("click",function () {
        var $btn = $(this).button('loading');
        var input = $("#run-code-stdin").val();
        var code = editor.getValue();
        var language = $("#languages option:selected").html();
        $.ajax( {
            url:path+"/submitServlet/inputServlet",// 跳转到
            data:{
                input:input,
                code:code,
                language:language,
            },
            type:'post',
            cache:false,
            dataType:'json',
            success:function(data) {
                if(data.success){
                    solution_id = data.key;
                    ide_tt = setInterval(askRes,1000);
                }else{
                    alert("提交失败!");
                    $btn.button('reset')
                }
            },
            error : function() {
                // view("异常！");
                console.log("异常！");
                $btn.button('reset')
            }
        });
    });
})