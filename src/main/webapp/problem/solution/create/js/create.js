var path,pid,id;
var testEditor;


function load_header(){
    $.get("../../../header.jsp", function (data) {
        $("#header").html(data);
        $("#problem").addClass("active");
        $("body").show();
    });
    $.get("../../../footer.jsp",function (data) {
        $("#footer").html(data);
    });
}

function load_editor()
{
    testEditor = editormd("test-editormd", {
        placeholder:'本编辑器支持Markdown编辑，左边编写，右边预览',  //默认显示的文字，这里就不解释了
        width: "90%",
        height: 640,
        syncScrolling: "single",
        path: "../../../js/editor.md-master/lib/",   //你的path路径（原资源文件中lib包在我们项目中所放的位置）
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
        imageUploadURL: '../../../blogServlet/imgServlet', //图片上传后台地址
        onload: function() {
            // 引入插件 执行监听方法
            editormd.loadPlugin("../../../js/editor.md-master/plugins/image-handle-paste/image-handle-paste", function(){
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
                "goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
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

    /*
    // or
    testEditor = editormd({
        id      : "test-editormd",
        width   : "90%",
        height  : 640,
        path    : "../lib/"
    });
    */
}

function init_text() {
    var textarea = document.getElementById('title');
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

function save(flag=true){
    id = $("#id").html();
    var title = $("#title").val();
    var text = $("#user_input").html();
    if(title.length == 0){
        if(flag) alert("标题不能为空");
        $("#sum").button('reset')
        return
    }
    if(text.length==0){
        if(flag) alert("正文不能为空");
        $("#sum").button('reset')
        return;
    }
    $.ajax( {
        url:path+"/blogServlet/saveServlet",// 跳转到
        data:{
            title:title,
            text:text,
            pid:pid,
            id:id,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                $("#id").html(data.key);
                if(flag) location.href = path + '/problem/solution/create/?pid='+ pid +'&id='+data.key;
            }else{
                alert(data.msg);
            }
            $("#sum").button('reset')
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
            $("#sum").button('reset')
        }
    });
}

function submit(){
    id = $("#id").html();
    var title = $("#title").val();
    var text = $("#user_input").val();
    if(title.length == 0){
        alert("标题不能为空");
        $("#ans").button('reset');
        return;
    }
    if(text.length==0){
        alert("正文不能为空");
        $("#ans").button('reset');
        return;
    }
    $.ajax( {
        url:path+"/blogServlet/submitServlet",// 跳转到
        data:{
            title:title,
            text:text,
            pid:pid,
            id:id,
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.success){
                $("#id").html(data.key);
                // OnlineJudge/problem/solution/?pid=1
                location.href = path + '/problem/solution/?pid='+pid;
                // location.href = path + '/problem/solution/create/?pid='+ pid +'&id='+data.key;
            }else{
                alert(data.msg);
            }
            $("#ans").button('reset')
        },
        error : function() {
            // view("异常！");
            console.log("异常！");
            $("#ans").button('reset')
        }
    });
}

$(function() {
    setTimeout("save(false);", 10000);
    load_header()
    load_editor();
    init_text();
    path = $("#path").html();
    pid = $("#pid").html();
    $('#sum').on('click', function () {
        $(this).button('loading')
        save();
    })
    $('#ans').on('click', function () {
        $(this).button('loading');
        submit();
    })
});