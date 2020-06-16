package OnlineJudge.web.servlet;

import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.TagDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.service.impl.hackServiceImpl;
import OnlineJudge.util.DelDirectory;
import OnlineJudge.util.ZIPUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/problemServlet/*")
public class ProblemServlet extends BaseServlet {
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    TagDaoImpl tagDao = new TagDaoImpl();
    public void creatServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info_key info_key = new info_key();
        User_password user = (User_password) request.getSession().getAttribute("User");
        String cid = request.getParameter("cid");
        String show_id = request.getParameter("show_id");
        String title = request.getParameter("title");
        String time_limit = request.getParameter("time_limit");
        String memory_limit = request.getParameter("memory_limit");

        if(show_id == null){
            info_key.setSuccess(false);
            info_key.setMsg("展示id不能为空");
            writeValue(info_key,response);
            return;
        }

        if(cid == null ){
            info_key.setSuccess(false);
            info_key.setMsg("比赛id为空");
            writeValue(info_key,response);
            return;
        }
        if(user == null){
            info_key.setSuccess(false);
            info_key.setMsg("未登录");
            writeValue(info_key,response);
            return;
        }

        Contest contest = contestsDao.findContestByCid(Integer.parseInt(cid));

        if(contest == null){
            info_key.setSuccess(false);
            info_key.setMsg("未找到该比赛");
            writeValue(info_key,response);
            return;
        }

        if(!"root".equals(user.getPower()) && contest.getMaster() != user.getId()){
            info_key.setSuccess(false);
            info_key.setMsg("没有权限进行操作");
            writeValue(info_key,response);
            return;
        }

        Problem problem = new Problem();
        problem.setContest_id(Integer.parseInt(cid));
        problem.setTitle(title);
        problem.setType(show_id);
        problem.setMaster(user.getId());
        problem.setTime_limit(Integer.parseInt(time_limit));
        problem.setMemory_limit(Double.parseDouble(memory_limit));
        problem.setIsspj(0);
        problem.setSource("Round #"+contest.getId()+" "+problem.getTitle());

        try{
            int pid = problemDao.insertProblem(problem);
            contestsDao.updateSum(Integer.parseInt(cid));

            String realPath = this.getServletContext().getRealPath("/data/" + pid);
            File file = new File(realPath);
            file.mkdirs();

            info_key.setSuccess(true);
            info_key.setKey(pid);
            info_key.setMsg("创建成功");
            writeValue(info_key,response);
        }catch (Exception e){
            System.out.println(e);
            info_key.setSuccess(false);
            info_key.setMsg("服务器错误");
            writeValue(info_key,response);
        }
    }
    public void updateServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();

        HttpSession session = request.getSession();
        User_password user =(User_password) session.getAttribute("User");

        if(user == null){
            info.setSuccess(false);
            info.setMsg("没有权限");
            writeValue(info,response);
            return;
        }

        //1.判断是否为multipart请求
        if(!ServletFileUpload.isMultipartContent(request)){
            info.setSuccess(false);
            info.setMsg("当前请求不支持文件上传");
            writeValue(info,response);
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
            upload.setFileSizeMax(1024*1024*100);
            //设置一次上传的所有文件的最大值
            upload.setSizeMax(1024*1024*200);

            //4.解析请求
            List<FileItem> items = upload.parseRequest(request);
            Problem problem = new Problem();
            String path = null,path2 =null;
            //遍历items
            for (FileItem item : items){
                if(item.isFormField()){//若item为普通表单项
                    String fieldName = item.getFieldName();//获取表单项名称
                    String value = item.getString("utf-8");//获取表单项值
                    if("cid".equals(fieldName)) problem.setContest_id(Integer.parseInt(value));
                    if("pid".equals(fieldName)){
                        if(value.length() == 0){
                            info.setSuccess(false);
                            info.setMsg("提交错误");
                            writeValue(info,response);
                            return;
                        }
                        Problem pro = problemDao.findProblemByPid(Integer.parseInt(value));

                        if("root".equals(user.getPower())&&pro.getMaster()!=user.getId()){
                            info.setSuccess(false);
                            info.setMsg("没有权限");
                            writeValue(info,response);
                            return;
                        }
                        path2 = "/data" + "/" + pro.getPid();
                        if(pro != null) problem.setPath(pro.getPath());
                        problem.setPid(Integer.parseInt(value));
                        problem.setMaster(pro.getMaster());
                    }
                    if("show_id".equals(fieldName)) problem.setType(value);
                    if("title".equals(fieldName)) problem.setTitle(value);
                    if("memory".equals(fieldName)) problem.setMemory_limit(Double.parseDouble(value));
                    if("time_limit".equals(fieldName)) problem.setTime_limit(Integer.parseInt(value));
                    if("pro_text".equals(fieldName)){
                        if( problem.getPath() != null && problem.getPath().length() != 0){
                            String oldPath = problem.getPath();
                            path = oldPath;
                            String realOldPath = this.getServletContext().getRealPath(oldPath);

                            File file = new File(realOldPath,"main.md");

                            if(file.exists()) file.delete();
                        }

                        if(path == null) {
                            path = "/problemdata";
                            path = path + "/" + problem.getPid();
                            problem.setPath(path);

                            try {
                                problemDao.updateProblem(problem);
                            }catch (Exception e){
                                info.setSuccess(false);
                                info.setMsg("数据库错误");
                                writeValue(info,response);
                                return;
                            }
                        }

                        String realPath = this.getServletContext().getRealPath(path);
                        File dirFile = new File(realPath);
                        if(!dirFile.exists()){ //若文件不存在则创建
                            dirFile.mkdirs();
                        }
                        //创建目标文件，保存上传的文件
                        File descFile=new File(realPath,"main.md");
                        FileOutputStream os = new FileOutputStream(descFile);//输出流
                        //输入流数据写入到输出流
                        os.write(value.getBytes());
                        os.close();
                    }
                    if("languages".equals(fieldName)){
                        String[] languages = value.split(",");
                        problemDao.removeLanguage(problem.getPid());
                        for(String language:languages){
                            try {
                                problemDao.addLanguage(problem.getPid(),language);
                            }catch (Exception e){
                                System.out.println(e);
                                info.setSuccess(false);
                                info.setMsg("添加语言失败");
                                writeValue(info,response);
                            }
                        }
                    }
                    if("code_temp".equals(fieldName)){
                        String realPath = this.getServletContext().getRealPath(path);
                        realPath = realPath + "/codeBase";
                        File dirFile = new File(realPath);
                        if(dirFile.exists()) DelDirectory.delAllFile(dirFile);
                        if(!dirFile.exists()){ //若文件不存在则创建
                            dirFile.mkdirs();
                        }
                        //创建目标文件，保存上传的文件
                        File descFile=new File(realPath,"code");
                        FileOutputStream os = new FileOutputStream(descFile);//输出流
                        //输入流数据写入到输出流
                        os.write(value.getBytes());
                        os.close();
                    }
                    if("spj".equals(fieldName)){
                        if(value==null||value.length()==0){
                            String realPath = this.getServletContext().getRealPath(path2);
                            realPath = realPath + "/spj/spj.cc";
                            File dirFile = new File(realPath);
                            dirFile.delete();
                            problem.setIsspj(0);
                            problemDao.updateProblem(problem);
                            continue;
                        }
                        problem.setIsspj(1);
                        problemDao.updateProblem(problem);
                        String realPath = this.getServletContext().getRealPath(path2);
                        realPath = realPath + "/spj";
                        File dirFile = new File(realPath);
                        if(dirFile.exists()) DelDirectory.delAllFile(dirFile);
                        System.out.println(realPath);
                        if(!dirFile.exists()){ //若文件不存在则创建
                            dirFile.mkdirs();
                        }
                        //创建目标文件，保存上传的文件
                        File descFile=new File(realPath,"spj.cc");
                        FileOutputStream os = new FileOutputStream(descFile);//输出流
                        //输入流数据写入到输出流
                        os.write(value.getBytes());
                        os.close();
                    }
                    if("interactive".equals(fieldName)){
                        String realPath = this.getServletContext().getRealPath(path);
                        realPath = realPath + "/interactive";
                        File dirFile = new File(realPath);
                        if(dirFile.exists()) DelDirectory.delAllFile(dirFile);
                        if(!dirFile.exists()){ //若文件不存在则创建
                            dirFile.mkdirs();
                        }
                        //创建目标文件，保存上传的文件
                        File descFile=new File(realPath,"interactive.cpp");
                        FileOutputStream os = new FileOutputStream(descFile);//输出流
                        //输入流数据写入到输出流
                        os.write(value.getBytes());
                        os.close();
                    }
                    if("tags".equals(fieldName)){
                        String[] tags = value.split(",");
                        problemDao.removeTag(problem.getPid());
                        for(String tag:tags){
                            if(tag.length() == 0) continue;
                            int tid = tagDao.findTag(tag);
                            if(tid == 0) tid = tagDao.addTag(tag);
                            try {
                                if(tagDao.findTag(problem.getPid(),tid) == 0) problemDao.addTag(problem.getPid(),tid);
                            }catch (Exception e){
                                System.out.println(e);
                                info.setSuccess(false);
                                info.setMsg("标签添加失败");
                                writeValue(info,response);
                                return;
                            }

                        }
                    }
                    if("data_in".equals(fieldName)){
                        String[] datas = value.split(",\\]\\|\\[,");
                        String realPath = this.getServletContext().getRealPath(path2);
                        File dirFile = new File(realPath);
                        if(dirFile.exists()) {
                            File[] files = dirFile.listFiles();
                            for(File file : files){
                                if("simple".equals(file.getName().split("\\d+\\.")[0])) file.delete();
                            }
                        }
                        if(!dirFile.exists()){ //若文件不存在则创建
                            dirFile.mkdirs();
                        }
                        for(int i=0;i<datas.length;i++){
                            String data = datas[i];
                            //创建目标文件，保存上传的文件
                            File descFile=new File(realPath,"simple" + (i+1) + ".in");
                            FileOutputStream os = new FileOutputStream(descFile);//输出流
                            //输入流数据写入到输出流
                            os.write(data.getBytes());
                            os.close();
                        }
                    }
                    if("data_out".equals(fieldName)){
                        String[] datas = value.split(",\\]\\|\\[,");
                        String realPath = this.getServletContext().getRealPath(path2);

                        for(int i=0;i<datas.length;i++){
                            String data = datas[i];
                            //创建目标文件，保存上传的文件
                            File descFile=new File(realPath,"simple" + (i+1) + ".out");
                            FileOutputStream os = new FileOutputStream(descFile);//输出流
                            //输入流数据写入到输出流
                            os.write(data.getBytes());
                            os.close();
                        }
                    }
                } else {//若item为文件表单项
                    String fileName = item.getName();//获取文件名
                    //获取最后一个.的位置
                    int lastIndexOf = fileName.lastIndexOf(".");
                    //获取文件的后缀名 .zip
                    fileName = fileName.substring(lastIndexOf);

                    fileName = problem.getPid() + fileName;

                    InputStream is = item.getInputStream();//获取输入流

                    String realPath = this.getServletContext().getRealPath(path2);

                    File dirFile = new File(realPath);
                    if(dirFile.exists()){
                        File[] files = dirFile.listFiles();
                        for(File file : files){
                            if(!"simple".equals(file.getName().split("\\d+\\.")[0])) file.delete();
                        }
                    }
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

                    ZIPUtils.unZip(descFile,realPath);
                    descFile.delete();
                }
                info.setSuccess(true);
                info.setMsg("提交成功");
                writeValue(info,response);
            }

        } catch (FileUploadException e) {
            info.setSuccess(false);
            info.setMsg("服务器错误!");
            writeValue(info,response);
            e.printStackTrace();
        }
    }
    public void updateCodeServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String pid = request.getParameter("pid");
        String code = request.getParameter("code");
        User_password user =(User_password) request.getSession().getAttribute("User");
        if(user == null){
            info.setSuccess(false);
            info.setMsg("用户未登录，无法保存");
            writeValue(info,response);
            return;
        }
        if(pid == null){
            info.setSuccess(false);
            info.setMsg("题目为空");
            writeValue(info,response);
            return;
        }
        Problem problem = problemDao.findProblemByPid(Integer.parseInt(pid));
        if(problem == null){
            info.setMsg("未找到该题目");
            info.setSuccess(false);
            writeValue(info,response);
            return;
        }
        problemDao.updateCode(Integer.parseInt(pid),user.getId(),code);
        info.setMsg("更新成功");
        info.setSuccess(true);
        writeValue(info,response);
    }
    public void addHackServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();

        HttpSession session = request.getSession();
        User_password user =(User_password) session.getAttribute("User");

        if(user == null){
            info.setSuccess(false);
            info.setMsg("请登陆后再进行操作");
            writeValue(info,response);
            return;
        }

        //1.判断是否为multipart请求
        if(!ServletFileUpload.isMultipartContent(request)){
            info.setSuccess(false);
            info.setMsg("当前请求不支持文件上传");
            writeValue(info,response);
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
            upload.setFileSizeMax(1024*1024*100);
            //设置一次上传的所有文件的最大值
            upload.setSizeMax(1024*1024*200);

            //4.解析请求
            List<FileItem> items = upload.parseRequest(request);
            Problem problem = new Problem();
            String path = null,path2 =null;
            //遍历items
            String realPath = "";
            List<String> hackName = new ArrayList<String>();
            List<String> hack_in = new ArrayList<String>();
            List<String> hack_out = new ArrayList<String>();
            String standard_code = "";
            String language = "";
            int sum_in = 0,sum_out = 0;
            for (FileItem item : items){
                if(item.isFormField()){//若item为普通表单项
                    String fieldName = item.getFieldName();//获取表单项名称
                    String value = item.getString("utf-8");//获取表单项值
                    if("pid".equals(fieldName)){
                        realPath = this.getServletContext().getRealPath("/data/" + value);
                        Standard_code standard = problemDao.getStandard_code(Integer.parseInt(value));
                        if(standard == null || standard.getCode().length() == 0){
                            info.setSuccess(false);
                            info.setMsg("未找到标程");
                            writeValue(info,response);
                            return;
                        }
                        standard_code = standard.getCode();
                        language = standard.getLanguage();
                    }
                    if("data_in".equals(fieldName)){
                        String[] datas = value.split(",\\]\\|\\[,");
                        File dirFile = new File(realPath);

                        if(!dirFile.exists()){ //若文件不存在则创建
                            dirFile.mkdirs();
                        }

                        if(hackName.size() == 0){
                            for(int i=0;i<datas.length;i++)
                                hackName.add("hack-" + user.getId() +"-" + i + new Date().getTime());
                        }


                        for(int i=0;i<datas.length;i++) hack_in.add(datas[i]);
                    }
                    if("data_out".equals(fieldName)){
                        String[] datas = value.split(",\\]\\|\\[,");
                        if(hackName.size() == 0){
                            for(int i=0;i<datas.length;i++)
                                hackName.add(user.getId() +"-" + i + new Date().getTime());
                        }
                        for(int i=0;i<datas.length;i++) hack_out.add(datas[i]);
                    }
                } else {//若item为文件表单项
                }
            }
            if(hack_in.size() != hack_out.size()){
                info.setSuccess(false);
                info.setMsg("上传失败，输入与输出不能一一对应");
                writeValue(info,response);
                return;
            }
            hackServiceImpl hackService = new hackServiceImpl();
            for(int i=0;i<hack_in.size();i++){
                System.out.println("check:"+hack_in.get(i)+"--"+hack_out.get(i));
                boolean check = hackService.check(standard_code, language, hack_in.get(i), hack_out.get(i));
                if(check){
                    String data_in = hack_in.get(i);
                    //创建目标文件，保存上传的文件
                    File descFile_in=new File(realPath,hackName.get(i) + ".in");
                    FileOutputStream os_in = new FileOutputStream(descFile_in);//输出流
                    //输入流数据写入到输出流
                    os_in.write(data_in.getBytes());
                    os_in.close();
                    sum_in++;
                    String data_out = hack_out.get(i);
                    //创建目标文件，保存上传的文件
                    File descFile_out=new File(realPath,hackName.get(i) + ".out");
                    FileOutputStream os_out = new FileOutputStream(descFile_out);//输出流
                    //输入流数据写入到输出流
                    os_out.write(data_out.getBytes());
                    os_out.close();
                    sum_out++;
                }
            }
            info.setSuccess(true);
            info.setMsg("成功提交"+Math.min(sum_in,sum_out)+"组数据");
            writeValue(info,response);
        } catch (FileUploadException | InterruptedException e) {
            info.setSuccess(false);
            info.setMsg("服务器错误!");
            writeValue(info,response);
            e.printStackTrace();
        }
    }
}
