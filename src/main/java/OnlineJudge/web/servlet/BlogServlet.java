package OnlineJudge.web.servlet;

import OnlineJudge.dao.impl.BlogDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.util.DownLoadUtils;
import OnlineJudge.util.JDBCUtils;
import OnlineJudge.util.ReadFileData;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

@WebServlet("/blogServlet/*")
public class BlogServlet extends BaseServlet {
    BlogDaoImpl blogDao = new BlogDaoImpl();
    public void imgServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/blog/imgs";
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

                    InputStream is = item.getInputStream();//获取输入流

                    //按日期创建目录

                    Calendar now = Calendar.getInstance();
                    int year=now.get(Calendar.YEAR);
                    int month=now.get(Calendar.MONTH)+1;
                    int day=now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" +year + "/" +month + "/" + day;

                    String realPath = this.getServletContext().getRealPath(path);

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
    public void saveServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//        title:title,
//        text:text,
//        pid:pid,
//        id:id,
        info_key info = new info_key();
        User_password user = (User_password) request.getSession().getAttribute("User");

        String title = request.getParameter("title");
        String text = request.getParameter("text");
        String pids = request.getParameter("pid");
        String ids = request.getParameter("id");
        if(pids == null ){
            info.setSuccess(false);
            info.setMsg("找不到题目");
            writeValue(info,response);
            return;
        }
        int pid = Integer.parseInt(pids);
        int id = 0;
        if(ids != null) id = Integer.parseInt(ids);
        if(id == 0){
            String path="/blog/date";
            Calendar now = Calendar.getInstance();
            int year=now.get(Calendar.YEAR);
            int month=now.get(Calendar.MONTH)+1;
            int day=now.get(Calendar.DAY_OF_MONTH);
            path = path + "/" +year + "/" +month + "/" + day;
            String fileName = System.currentTimeMillis() + ".md";

            String realPath = this.getServletContext().getRealPath(path);

            File dirFile = new File(realPath);
            if(!dirFile.exists()){ //若文件不存在则创建
                dirFile.mkdirs();
            }

            File descFile=new File(realPath,fileName);
            FileOutputStream os = new FileOutputStream(descFile);
            os.write(text.getBytes());

            int i = blogDao.addBlog(user.getId(), pid, path+"/"+fileName, title);

            info.setKey(i);
            info.setSuccess(true);

            writeValue(info,response);
            return;
        }

        Solution blogById = blogDao.findBlogById(id);
        String path = blogById.getPath();
        String realPath = this.getServletContext().getRealPath(path);
        File descFile=new File(realPath);
        FileOutputStream os = new FileOutputStream(descFile);
        os.write(text.getBytes());

        blogDao.updateBlog(id,title);

        info.setSuccess(true);
        info.setMsg("保存成功");
        info.setKey(id);
        writeValue(info,response);
    }
    public void submitServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info_key info = new info_key();
        User_password user = (User_password) request.getSession().getAttribute("User");

        String title = request.getParameter("title");
        String text = request.getParameter("text");
        String pids = request.getParameter("pid");
        String ids = request.getParameter("id");
        if(pids == null ){
            info.setSuccess(false);
            info.setMsg("找不到题目");
            writeValue(info,response);
            return;
        }
        int pid = Integer.parseInt(pids);
        int id = 0;
        if(ids != null) id = Integer.parseInt(ids);
        if(id == 0){
            String path="/blog/date";
            Calendar now = Calendar.getInstance();
            int year=now.get(Calendar.YEAR);
            int month=now.get(Calendar.MONTH)+1;
            int day=now.get(Calendar.DAY_OF_MONTH);
            path = path + "/" +year + "/" +month + "/" + day;
            String fileName = System.currentTimeMillis() + ".md";

            String realPath = this.getServletContext().getRealPath(path);

            File dirFile = new File(realPath);
            if(!dirFile.exists()){ //若文件不存在则创建
                dirFile.mkdirs();
            }

            File descFile=new File(realPath,fileName);
            FileOutputStream os = new FileOutputStream(descFile);
            os.write(text.getBytes());

            int i = blogDao.addBlog(user.getId(), pid, path+"/"+fileName, title);

            blogDao.summitBlog(i);
            info.setSuccess(true);
            info.setMsg("提交成功");
            info.setKey(i);
            writeValue(info,response);
            return;
        }

        Solution blogById = blogDao.findBlogById(id);
        String path = blogById.getPath();
        String realPath = this.getServletContext().getRealPath(path);
        File descFile=new File(realPath);
        FileOutputStream os = new FileOutputStream(descFile);
        os.write(text.getBytes());

        blogDao.updateBlog(id,title);
        blogDao.summitBlog(id);
        info.setSuccess(true);
        info.setMsg("提交成功");
        info.setKey(id);
        writeValue(info,response);
    }
    public void showServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String ids = request.getParameter("id");
        if(ids == null){
            info.setSuccess(false);
            info.setMsg("访问异常");
            writeValue(info,response);
            return;
        }
        int id = Integer.parseInt(ids);

        Solution blog = blogDao.findBlogById(id);

        String path = blog.getPath();

        String realPath = this.getServletContext().getRealPath(path);

        File file = new File(realPath);
        String text = ReadFileData.txt2String(file,true);

        System.out.println(text);

        info.setSuccess(true);
        info.setMsg(text);
        writeValue(info,response);

    }
    public void changeLoveServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        User_password  user = (User_password) request.getSession().getAttribute("User");
        int uid = user.getId();

        String bid = request.getParameter("bid");

        info_key info = new info_key();

        if(bid == null ){
            info.setSuccess(false);
            info.setMsg("找不到题解");
            writeValue(info,response);
            return;
        }
        int id = Integer.parseInt(bid);
        int love = blogDao.findLove(uid, id);


        if(love == 0){
            int che = blogDao.checkLove(uid, id);
            if(che == 0){
                blogDao.addLove(uid,id);
            }else{
                blogDao.updateLove(uid,id,1);
            }
            blogDao.changeLove(id,1);
            info.setKey(1);
        }else{
            blogDao.updateLove(uid,id,0);
            blogDao.changeLove(id,-1);
            info.setKey(0);
        }

        info.setSuccess(true);
        Solution blog = blogDao.findBlogById(id);
        info.setMsg(blog.getLove()+"");
        writeValue(info,response);
    }
    public void delServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String pids = request.getParameter("pid");
        String ids = request.getParameter("id");

        if(pids == null){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }
        String path = request.getContextPath() + "/problem/solution/?pid="+pids;

        if(ids == null ){
            request.getRequestDispatcher(path).forward(request,response);
            return;
        }
        int pid = Integer.parseInt(pids);
        int id = Integer.parseInt(ids);

        Solution blog = blogDao.findBlogById(id);

        User_password user = (User_password) request.getSession().getAttribute("User");
        if(user.getId() != blog.getMaster() ){
//            request.getRequestDispatcher(path).forward(request,response);
            response.sendRedirect(path);
            return;
        }

        blogDao.delBlogAndLove(id);
        response.sendRedirect(path);

    }
}
