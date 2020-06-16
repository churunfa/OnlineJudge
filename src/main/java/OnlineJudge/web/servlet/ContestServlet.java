package OnlineJudge.web.servlet;

import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.TagDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.util.DelDirectory;
import OnlineJudge.util.DownLoadUtils;
import OnlineJudge.util.ReadFileData;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.Line;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet("/contestServlet/*")
public class ContestServlet extends BaseServlet {
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    public void imgServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/contestdata/imgs";
        uploadimg img = new uploadimg();
        //1.判断是否为multipart请求
        if(!ServletFileUpload.isMultipartContent(request)){
            img.setUrl("");
            img.setSuccess(false);
            img.setMessage("当前请求不支持文件上传");
            writeValue(img,response);
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //设置临时文件的边界值，大于该值会先保存在临时文件中
            //单位：字节

            //factory.setSizeThreshold(1024*1024*1);//1M

            //设置临时文件
//            String tempPath = this.getServletContext().getRealPath("/temp");
//            File temp = new File(tempPath);
//            factory.setRepository(temp);


            //3.创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);

            //设置每一个item头部字符编码
            upload.setHeaderEncoding("utf-8");

            //设置单个文件的最大值
            upload.setFileSizeMax(1024*1024*10);
            //设置一次上传的所有文件的最大值
            upload.setSizeMax(1024*1024*50);

            //4.解析请求
            List<FileItem> items = upload.parseRequest(request);

            //遍历items
            for (FileItem item : items){
                if(item.isFormField()){//若item为普通表单项
                    String fieldName = item.getFieldName();//获取表单项名称
                    String value = item.getString("utf-8");//获取表单项值
                } else {//若item为文件表单项
                    String fileName = item.getName();//获取文件名
                    //获取最后一个.的位置
                    int lastIndexOf = fileName.lastIndexOf(".");
                    //获取文件的后缀名 .jpg
                    fileName = fileName.substring(lastIndexOf);

                    fileName = System.currentTimeMillis()+fileName;
                    System.out.println(fileName);
                    InputStream is = item.getInputStream();//获取输入流

                    //按日期创建目录

                    Calendar now = Calendar.getInstance();
                    int year=now.get(Calendar.YEAR);
                    int month=now.get(Calendar.MONTH)+1;
                    int day=now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" +year + "/" +month + "/" + day;

                    String realPath = this.getServletContext().getRealPath(path);
                    System.out.println(realPath);
                    File dirFile = new File(realPath);
                    if(!dirFile.exists()){ //若文件不存在则创建
                        dirFile.mkdirs();
                    }
                    //创建目标文件，保存上传的文件
                    File descFile=new File(realPath,fileName);
                    FileOutputStream os = new FileOutputStream(descFile);//输出流
                    //输入流数据写入到输出流
                    int len=-1;
                    byte []buff=new byte[1024*8];
                    while((len=is.read(buff))!=-1){
                        os.write(buff);
                    }
                    os.close();
                    is.close();
                    //删除临时文件
                    response.setContentType("application/json;charset=utf-8");
                    item.delete();
//                    String agent = request.getHeader("user-agent");
//                    fileName = DownLoadUtils.getFileName(agent,fileName);
                    img.setUrl(request.getContextPath() + path+"/"+fileName);
                    img.setSuccess(true);
                    img.setMessage("图片上传成功");
                    writeValue(img,response);
                }
            }

        } catch (FileUploadException e) {
            img.setUrl("");
            img.setSuccess(false);
            img.setMessage("图片上传失败");
            writeValue(img,response);
            e.printStackTrace();
        }
    }
    public void textServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String pid = request.getParameter("pid");
        if(pid == null){
            info.setSuccess(false);
            info.setMsg("pid不能为空");
            writeValue(info,response);
            return;
        }
        Problem problem = problemDao.findProblemByPid(Integer.parseInt(pid));
        String path = problem.getPath();
        String title = "<h1 style='text-align: center'>" + problem.getTitle() + "</h1>";

        if(path == null ){
            info.setSuccess(false);
            info.setMsg("未找到该题目");
            writeValue(info,response);
            return;
        }

        String url = path + "/main.md";
        String realPath = this.getServletContext().getRealPath(url);
        url = request.getContextPath() + "/" + url;

        String text = ReadFileData.txt2String(new File(realPath));

        info.setSuccess(true);
        info.setMsg(title + text);
        writeValue(info,response);
    }
    public void signServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        User_password user = (User_password) request.getSession().getAttribute("User");
        String id = request.getParameter("id");
        info info = new info();

        if(id == null||user == null){
            info.setSuccess(false);
            info.setMsg("报名失败");
            writeValue(info,response);
            return;
        }

        if(contestsDao.checkSign(user.getId(),Integer.parseInt(id)) > 0){
            info.setSuccess(false);
            info.setMsg("您已报名，请不要重复报名");
            writeValue(info,response);
            return;
        }

        Contest contest = contestsDao.findContestByCid(Integer.parseInt(id));
        Long st = contest.getStart_time().getTime();
        Long now = new Date().getTime();

        if(st < now){
            info.setSuccess(false);
            info.setMsg("不在报名时间内");
            writeValue(info,response);
        }

        try {
            contestsDao.sign(user.getId(),Integer.parseInt(id));
            info.setSuccess(true);
            info.setMsg("报名成功");
            writeValue(info,response);

        }catch (Exception e){
            info.setSuccess(false);
            info.setMsg("服务器错误，请联系管理员！");
            writeValue(info,response);
        }

    }
    public void updateServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String cid = request.getParameter("cid");
        String title = request.getParameter("title");
        String st = request.getParameter("st");
        String ed = request.getParameter("ed");
        String type = request.getParameter("type");
        String msg = request.getParameter("msg");

        User_password user = (User_password) request.getSession().getAttribute("User");
        if(user == null){
            info.setSuccess(false);
            info.setMsg("您未登录");
            writeValue(info,response);
            return;
        }

        if(Long.parseLong(ed) < new Date().getTime()){
            info.setSuccess(false);
            info.setMsg("结束时间不能早于当前时间");
            writeValue(info,response);
            return;
        }
        if(Long.parseLong(ed) < Long.parseLong(st)){
            info.setSuccess(false);
            info.setMsg("开始时间不能早于结束时间");
            writeValue(info,response);
            return;
        }

        if("normal".equals(user.getPower())&&
                !("div3".equals(type)&&user.getLv()>=1||
                 "div2".equals(type)&&user.getLv()>=3||
                 "div1".equals(type)&&user.getLv()>=4)){
            info.setSuccess(false);
            info.setMsg("您没有权限创建此类比赛");
            writeValue(info,response);
            return;
        }

        if("normal".equals(user.getPower())&&!"div1".equals(type)&&!"div2".equals(type)&&!"div3".equals(type)){
            info.setSuccess(false);
            info.setMsg("您没有权限创建此类比赛");
            writeValue(info,response);
            return;
        }

        Contest contest = new Contest();
        if(cid != null){
            contest = contestsDao.findContestByCid(Integer.parseInt(cid));

            contest.setName(title);
            contest.setStart_time(new Date(Long.parseLong(st)));
            contest.setEnd_time(new Date(Long.parseLong(ed)));
            contest.setType(type);
            contest.setNotice(msg);
            try {
                contestsDao.updateContest(contest);
            }catch (Exception e){
                System.out.println(e);
                info.setSuccess(false);
                info.setMsg("服务器错误");
                writeValue(info,response);
                return;
            }
            try{
                request.getRequestDispatcher("/quartz/remove?id="+cid).forward(request, response);
            }catch (Exception e){
                info.setSuccess(true);
                info.setMsg("创建成功");
                writeValue(info,response);
            }
            return;
//          response.sendRedirect(request.getContextPath()+"/quartz/remove?id="+cid);

        }

        contest.setName(title);
        contest.setStart_time(new Date(Long.parseLong(st)));
        contest.setEnd_time(new Date(Long.parseLong(ed)));
        contest.setType(type);
        contest.setNotice(msg);
        contest.setMaster(user.getId());

        int id =0 ;
        try{
            id = contestsDao.addContest(contest);
        }catch (Exception e){
            info.setSuccess(false);
            info.setMsg("服务器错误");
            writeValue(info,response);
            return;
        }
//      response.sendRedirect(request.getContextPath()+"/quartz/showProblem?id="+id);
        try{
            request.getRequestDispatcher("/quartz/showProblem?id="+id).forward(request, response);
        }catch (Exception e){
            info.setSuccess(true);
            info.setMsg("创建成功");
            writeValue(info,response);
        }


    }
    public void findContestServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info_up<Contest> info = new info_up<Contest>();
        String cid = request.getParameter("cid");

        if(cid == null){
            info.setSuccess(false);
            info.setMsg("比赛id为空");
            writeValue(info,response);
            return;
        }

        Contest contest = contestsDao.findContestByCid(Integer.parseInt(cid));

        if(contest == null){
            info.setSuccess(false);
            info.setMsg("未找到该比赛");
            writeValue(info,response);
            return;
        }
        info.setSuccess(true);
        info.setMsg("成功");
        info.setData(contest);
        writeValue(info,response);
    }
    public void askNotice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String cid = request.getParameter("cid");
        if(cid == null){
            info.setSuccess(false);
            info.setMsg("cid为空");
            writeValue(info,response);
            return;
        }
        Contest contest = contestsDao.findContestByCid(Integer.parseInt(cid));
        if(contest == null){
            info.setSuccess(false);
            info.setMsg("未找到该比赛");
            writeValue(info,response);
            return;
        }
        String text = contest.getNotice();
        String regex = "\\$\\$";
        text = text.replaceAll("\\$\\$","\\$");
//        regex = "<";
//        text = text.replaceAll(regex,"&lt;");
//        regex = ">";
//        text = text.replaceAll(regex,"&gt;");
        info.setSuccess(true);
        info.setMsg(text);
        writeValue(info,response);
    }
    public void updateNotice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String cid = request.getParameter("cid");
        String msg = request.getParameter("msg");
        User_password user = (User_password) request.getSession().getAttribute("User");
        if(cid == null){
            info.setSuccess(false);
            info.setMsg("cid为空");
            writeValue(info,response);
            return;
        }

        Contest contest = contestsDao.findContestByCid(Integer.parseInt(cid));

        if(!"root".equals(user.getPower()) && contest.getMaster() != user.getId()){
            info.setSuccess(false);
            info.setMsg("您没有权限进行修改");
            writeValue(info,response);
            return;
        }

        contest.setNotice(msg);

        try{
            contestsDao.updateContest(contest);
            info.setSuccess(true);
            info.setMsg("更新成功");
            writeValue(info,response);
        }catch (Exception e){
            info.setSuccess(true);
            info.setMsg("服务器错误");
            writeValue(info,response);
        }


    }
    public void askNowTime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        info_up<ContestTime> info = new info_up<ContestTime>();
        String cid = request.getParameter("cid");
        if(cid == null ){
            info.setSuccess(false);
            info.setMsg("比赛id为空");
            writeValue(info,response);
            return;
        }

        Contest contest = contestsDao.findContestByCid(Integer.parseInt(cid));
        if(contest == null){
            info.setSuccess(false);
            info.setMsg("未找到该比赛");
            writeValue(info,response);
            return;
        }

        try{
            ContestTime contestTime = new ContestTime();
            contestTime.setNow(new Date().getTime());
            contestTime.setSt(contest.getStart_time().getTime());
            contestTime.setEd(contest.getEnd_time().getTime());
            Long tot = contestTime.getEd() - contestTime.getSt();
            Long now_ = contestTime.getNow() - contestTime.getSt();

            if(tot<=0) contestTime.setR((long)0);
            else if(now_<=0) contestTime.setR((long)0);
            else if(now_>tot) contestTime.setR((long)100);
            else  contestTime.setR(now_*100/tot);
            info.setData(contestTime);
            info.setSuccess(true);
            info.setMsg("请求成功");
            writeValue(info,response);
        }catch (Exception e){
            info.setMsg("请求失败");
            info.setSuccess(false);
            writeValue(info,response);
        }
    }
    public void delProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String pid = request.getParameter("id");
        if(pid == null){
            writeValue("题目id为空",response);
            return;
        }

        Problem problem = problemDao.findProblemByPid(Integer.parseInt(pid));

        if(problem == null){
            writeValue("未找到题目",response);
            return;
        }

        User_password user = (User_password) request.getSession().getAttribute("User");

        if(user == null){
            writeValue("请先未登录",response);
            return;
        }

        if(!"root".equals(user) && problem.getMaster() != user.getId()){
            writeValue("没有权限进行操作",response);
            return;
        }

        try {

            File file = null;
            if(problem.getPath() != null) file = new File(this.getServletContext().getRealPath(problem.getPath()));
            if(file != null && file.exists()) DelDirectory.delAllFile(file);
            String realPath = this.getServletContext().getRealPath("/data");
            file = new File(realPath+"/"+problem.getPid());
            System.out.println(file);
            if(file.exists()) DelDirectory.delAllFile(file);

            problemDao.removeTag(problem.getPid());
            problemDao.delProblem(problem.getPid(),problem.getContest_id());
            response.sendRedirect(request.getContextPath()+"/contests/contests_show?id=" + problem.getContest_id());
            return;
        }catch (Exception e){
            writeValue("服务器错误",response);
        }
    }
}
