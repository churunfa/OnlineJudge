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
function compile(text){

    //创建实例
    var converter = new showdown.Converter();
    //进行转换
    var html = converter.makeHtml(text);
    //展示到对应的地方  result便是id名称
    // document.getElementById("result").innerHTML = html;
    return html;
}

$(function () {
    $.get("../header.jsp", function (data) {
        $("#header").html(data);
        // $("").addClass("active");
        $("body").show();
    });
    $.get("../footer.jsp",function (data) {
        $("#footer").html(data);
    });
    var md = "```\n"+$("#get_code").val()+"\n```";
    $("#code").html(compile(md));
})