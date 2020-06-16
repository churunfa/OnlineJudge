package OnlineJudge.web.servlet;

import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.QueueDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.util.LanguageUtils;
import OnlineJudge.util.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/submitServlet/*")
public class SubmitServlet extends BaseServlet  {
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    QueueDaoImpl queueDao = new QueueDaoImpl();
    public void submitCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        /**
         * language
         * pid
         * code
         */

        info info = new info();
        String language = request.getParameter("language");
        if(language == null){
            info.setMsg("请选择编程语言");
            info.setSuccess(false);
            writeValue(info,response);
            return;
        }
        String pid = request.getParameter("pid");
        if(pid == null){
            info.setSuccess(false);
            info.setMsg("请选择题目");
            writeValue(info,response);
            return;
        }


        User_password user = (User_password) request.getSession().getAttribute("User");
        if(user == null){
            info.setMsg("请登陆后再提交");
            info.setSuccess(false);
            writeValue(info,response);
            return;
        }

        Problem problem = problemDao.findProblemByPid(Integer.parseInt(pid));
        if(problem == null){
            info.setSuccess(false);
            info.setMsg("未来找到该题目");
            writeValue(info,response);
            return;
        }
        QueueInfo queueInfo = new QueueInfo();

        String code = request.getParameter("code");

        queueInfo.setCode_length(code.getBytes().length);
        queueInfo.setContest_id(problem.getContest_id());
        queueInfo.setIn_date(new Date());
        queueInfo.setIp(request.getRemoteAddr());
        queueInfo.setLanguage(LanguageUtils.stringtoid(language));
        queueInfo.setProblem_id(problem.getPid());
        queueInfo.setResult(0);
        queueInfo.setUser_id(""+user.getId());

        source_code source_code = new source_code();
        source_code.setSource(code);

        Status sta = new Status();
        sta.setCode_type(language);
        sta.setIs_show(false);
        sta.setLength(code.getBytes().length);
        sta.setProblem_id(problem.getPid());
        sta.setMemory(0);
        sta.setRun_time(0);
        sta.setSub_time(new Date());
        sta.setUid(user.getId());
        sta.setStatus("等待测试");
        try {
            queueDao.commitInfo(queueInfo,source_code,sta);
            info.setMsg("提交成功");
            info.setSuccess(true);
            writeValue(info,response);
        }catch (Exception e){
            System.out.println(e);
            info.setSuccess(false);
            info.setMsg("提交失败");
            writeValue(info,response);
        }
    }
    public void inputServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String input = request.getParameter("input");
        String code = request.getParameter("code");
        String language = request.getParameter("language");

        info_key info = new info_key();
        User_password user = (User_password) request.getSession().getAttribute("User");

        if(user == null){
            info.setSuccess(true);
            info.setMsg("请先登录");
            return;
        }

        QueueInfo queueInfo = new QueueInfo();

        queueInfo.setCode_length(code.getBytes().length);
        queueInfo.setContest_id(0);
        queueInfo.setIn_date(new Date());
        queueInfo.setIp(request.getRemoteAddr());
        queueInfo.setLanguage(LanguageUtils.stringtoid(language));
        queueInfo.setProblem_id(0);
        queueInfo.setResult(0);
        queueInfo.setUser_id(""+user.getId());

        source_code source_code = new source_code();
        source_code.setSource(code);

        custominput custominput = new custominput();
        custominput.setInput_text(input);

        int solution_id = queueDao.insertInput(queueInfo,source_code,custominput);

        info.setMsg("上传成功");
        info.setSuccess(true);
        info.setKey(solution_id);
        writeValue(info,response);
    }
    public void getResultServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String id = request.getParameter("solution_id");

        info info = new info();
        if(id == null){
            info.setSuccess(false);
            info.setMsg("未指定提交id");
            writeValue(info,response);
            return;
        }
        int res = -1;
        try{
            res =queueDao.findResInfoBySid(Integer.parseInt(id));
        }catch (Exception e){

        }
        if(res == -1){
            info.setMsg("获取失败");
            info.setSuccess(false);
            writeValue(info,response);
            return;
        }

        if (res <= 3){
            info.setSuccess(false);
            info.setMsg("未完成");
            writeValue(info,response);
            return;
        }

        info.setSuccess(true);
        if(res == 4) info.setMsg("正常结束");
        else info.setMsg(Result.intToString(res));
        writeValue(info,response);
    }
    public void askOutServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String id = request.getParameter("solution_id");
        info info = new info();
        if(id == null){
            info.setMsg("提交id为空");
            info.setSuccess(false);
            writeValue(info,response);
            return;
        }
        String runtimeInfo = queueDao.findRuntimeInfo(Integer.parseInt(id));
        String compileInfo = queueDao.findCompileInfo(Integer.parseInt(id));
        if(runtimeInfo == null && compileInfo == null){
            info.setMsg("未查询到结果");
            info.setSuccess(false);
            writeValue(info,response);
        }
        info.setSuccess(true);
        if(runtimeInfo != null) info.setMsg(runtimeInfo);
        else info.setMsg(compileInfo);
        writeValue(info,response);
    }
    public void askResServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String id = request.getParameter("solution_id");
        info info = new info();
        if(id == null){
            info.setSuccess(false);
            info.setMsg("提交id为空");
            writeValue(info,response);
            return;
        }

        int res = queueDao.findResInfoBySid(Integer.parseInt(id));

        if(res == 4) info.setMsg("正常结束");
        else info.setMsg(Result.intToString(res));

        info.setSuccess(true);
        writeValue(info,response);
    }
    public void getLanguage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pid = request.getParameter("pid");
        info_up<List<String>> info = new info_up<>();
        if(pid == null){
            info.setSuccess(false);
            info.setMsg("pid为空");
            writeValue(info,response);
            return;
        }
        List<String> languages = problemDao.findLanguages(Integer.parseInt(pid));
        info.setData(languages);
        info.setSuccess(true);
        writeValue(info,response);
    }
    public void updateStandardServlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User_password user = (User_password) request.getSession().getAttribute("User");
        info info = new info();
        if(user == null){
            info.setSuccess(false);
            info.setMsg("请登陆之后再操作");
            writeValue(info,response);
            return;
        }

        String pid = request.getParameter("pid");
        String language = request.getParameter("language");
        String code = request.getParameter("code");
        if(pid == null){
            info.setSuccess(false);
            info.setMsg("未找到题目id");
            writeValue(info,response);
            return;
        }
        if(code == null){
            info.setSuccess(false);
            info.setMsg("未找到提交代码");
            writeValue(info,response);
            return;
        }

        Problem problem = problemDao.findProblemByPid(Integer.parseInt(pid));

        if(problem == null){
            info.setSuccess(false);
            info.setMsg("未找到题目");
            writeValue(info,response);
            return;
        }

        if(!"root".equals(user.getPower())&&user.getId()!=problem.getMaster()){
            info.setSuccess(false);
            info.setMsg("没有权限进行操作");
            writeValue(info,response);
            return;
        }

        Standard_code standard_code = problemDao.getStandard_code(problem.getPid());
        try{
            if(standard_code == null) problemDao.standard_codeAdd(problem.getPid(),code,language);
            else problemDao.standard_codeSet(problem.getPid(),code,language);
            info.setSuccess(true);
            info.setMsg("提交成功");
            writeValue(info,response);
        }catch (Exception e){
            info.setSuccess(false);
            info.setMsg("服务器错误");
            writeValue(info,response);
        }


    }
}
