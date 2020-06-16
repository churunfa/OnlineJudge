package OnlineJudge.web.servlet;

import OnlineJudge.dao.impl.UserDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.service.UserService;
import OnlineJudge.service.impl.UserServiceImpl;
import OnlineJudge.util.DownLoadUtils;
import OnlineJudge.util.MailUtils;
import OnlineJudge.util.Md5Util;
import OnlineJudge.util.UuidUtil;
import com.alibaba.druid.sql.visitor.functions.Substring;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.filefilter.FalseFileFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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

@WebServlet("/userServlet/*")
public class UserServlet extends BaseServlet {
    UserServiceImpl userService = new UserServiceImpl();
    public void imgServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        uploadimg img = new uploadimg();

        HttpSession session = request.getSession();
        User_password user =(User_password) session.getAttribute("User");
        String oldPath = user.getHead_img();

        String path = "/userdata/imgs";
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
                    if("id".equals(fieldName)){
                        if(Integer.parseInt(value) != user.getId()){
                            img.setSuccess(false);
                            img.setMessage("您没有权限对其进行修改");
                            writeValue(img,response);
                            return;
                        }
                    }
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

                    img.setUrl(path+"/"+fileName);
                    img.setSuccess(true);
                    img.setMessage("图片上传成功");
                    writeValue(img,response);

                    user.setHead_img(path+"/"+fileName);
                    userService.update(user);
                    request.getSession().setAttribute("User",user);
                    request.setAttribute("User",user);

                    if("/userdata/imgs/user.png".equals(oldPath)) return;

                    String realOldPath = this.getServletContext().getRealPath(oldPath);
                    boolean result = false;
                    File file = new File(realOldPath);
                    if (file.exists()) {
                        file.delete();
                        result = true;
                        System.out.println("文件已经被成功删除");
                    }


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
    public void findUserServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserLoginMsg userLoginMsg = new UserLoginMsg();
        User_password user = (User_password) request.getSession().getAttribute("User");
        if(user == null){
            Cookie[] cookies = request.getCookies();

            String userName = null;
            String userPassword = null;

            if(cookies != null){
                for(Cookie cookie : cookies){
                    String name = cookie.getName();
                    String value = cookie.getValue();
                    if("username".equals(name)) userName = value;
                    if("password".equals(name)) userPassword = value;
                }
                if(userName != null && userPassword !=null){
                    User_password cookieUser = new User_password();
                    cookieUser.setPassword(userPassword);
                    boolean flag = true;
                    for(int i=0;i<userName.length();i++) if(userName.charAt(i)=='@') flag=false;

                    if(flag) cookieUser.setUid(userName);
                    else cookieUser.setEmail(userName);

                    User_password login = userService.login(cookieUser);

                    if(login != null ){
                        userLoginMsg.setSuccess(true);
                        userLoginMsg.setMsg("用户已登录");
                        userLoginMsg.setUser(login);
                        writeValue(userLoginMsg,response);
                        request.getSession().setAttribute("User",login);
                        return;
                    }
                }
            }

            userLoginMsg.setSuccess(false);
            userLoginMsg.setMsg("用户未登录");
            writeValue(userLoginMsg,response);
            return;
        }
        userLoginMsg.setSuccess(true);
        userLoginMsg.setMsg("用户已登录");
        userLoginMsg.setUser(user);
        writeValue(userLoginMsg,response);
    }
    public void loginServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        UserLoginMsg userLoginMsg = new UserLoginMsg();
        String checkCode = request.getParameter("check_code");
        HttpSession session = request.getSession();
        String real_checkcode =(String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if(real_checkcode==null){
            userLoginMsg.setSuccess(false);
            userLoginMsg.setMsg("验证码失效");
            writeValue(userLoginMsg,response);
            return;
        }
        if(!real_checkcode.equalsIgnoreCase(checkCode)){
            System.out.println("验证码错误");
            userLoginMsg.setSuccess(false);
            userLoginMsg.setMsg("验证码错误");
            writeValue(userLoginMsg,response);
            return;
        }
        String user_id = request.getParameter("user_id");
        String user_password = request.getParameter("user_password");

        User_password user = new User_password();
        user.setPassword(user_password);
        boolean flag = true;
        for(int i=0;i<user_id.length();i++) if(user_id.charAt(i)=='@') flag=false;

        if(flag) user.setUid(user_id);
        else user.setEmail(user_id);


        User_password login = userService.login(user);

        if(login == null){
            userLoginMsg.setSuccess(false);
            userLoginMsg.setMsg("用户名或密码错误");
            writeValue(userLoginMsg,response);
            return;
        }

        Cookie usernameCookie = new Cookie("username", user_id);
        usernameCookie.setMaxAge(60*60*24*30);
        usernameCookie.setPath("/");
        response.addCookie(usernameCookie);

        Cookie passwordCookie = new Cookie("password", user_password);
        passwordCookie.setMaxAge(60*60*24*30);
        passwordCookie.setPath("/");
        response.addCookie(passwordCookie);

        request.getSession().setAttribute("User",login);
        userLoginMsg.setSuccess(true);
        userLoginMsg.setMsg("登录成功");
        userLoginMsg.setUser(login);
        writeValue(userLoginMsg,response);
    }
    public void exitServlet(HttpServletRequest request, HttpServletResponse response){
        request.getSession().invalidate();
        Cookie[] cookies = request.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                String name = cookie.getName();
                String value = cookie.getValue();
                if("username".equals(name)){
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                if("password".equals(name)){
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
    public void registerServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    /*
            name : name,
            uid : uid,
            password:password,
            check_code : check_code,
    */
        String name = request.getParameter("name");
        String uid = request.getParameter("uid");
        String password = request.getParameter("password");
        String check_code = request.getParameter("check_code");
        String email = request.getParameter("email");

        HttpSession session = request.getSession();
        String real_checkcode =(String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");

        UserLoginMsg userLoginMsg = new UserLoginMsg();

        if(real_checkcode==null){
            userLoginMsg.setSuccess(false);
            userLoginMsg.setMsg("验证码超时");
            writeValue(userLoginMsg,response);
            return;
        }
        if(!real_checkcode.equalsIgnoreCase(check_code)){
            userLoginMsg.setSuccess(false);
            userLoginMsg.setMsg("验证码错误");
            writeValue(userLoginMsg,response);
            return;
        }

        User_password user = new User_password();
        user.setName(name);
        user.setUid(uid);
        user.setPassword(password);
        user.setEmail(email);



        info register = userService.register(user);
        writeValue(register,response);
    }
    public void findByIdServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String id = request.getParameter("id");
        Object attribute = request.getSession().getAttribute("User" + id);
        if(attribute != null ) {
            info.setSuccess(true);
            info.setMsg("session中已经存在用户信息");
            writeValue(info,response);
            return;
        }
        User user = userService.find(id);

        if(user == null){
            info.setSuccess(false);
            info.setMsg("没有此用户");
            writeValue(info,response);
            return;
        }

        request.setAttribute("User",user);
        info.setSuccess(true);
        info.setMsg("用户信息查询成功");
        writeValue(info,response);
    }
    public void updateServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        int visit_id = Integer.parseInt(request.getParameter("visit_id"));
        User_password user = (User_password) request.getSession().getAttribute("User");
        String uid = user.getUid();
        if(user.getId() != visit_id && !"root".equals(user.getPower()) && "admin".equals(user.getPower()) ){
            info.setSuccess(false);
            info.setMsg("抱歉，您没有权限进行修改");
            writeValue(info,response);
            return;
        }

        String re = ">(.*?)<";
        String name = request.getParameter("name");
        String oldName = user.getName();

        name = oldName.replaceAll(re,">"+name+"<");
        System.out.println(name);

        user.setName(name);
        user.setUid(request.getParameter("uid"));
        user.setMajor(request.getParameter("major"));
        user.setGrade(request.getParameter("grade"));
        user.setSex(request.getParameter("sex"));

        if(!"男".equals(user.getSex())&&!"女".equals(user.getSex())&&"保密".equals(user.getSex())){
            info.setSuccess(false);
            info.setMsg("性别不合法");
            writeValue(info,response);
            return;
        }

       if(!uid.equals(user.getUid())){
           User byUid = userService.findByUid(user.getUid());
           if(byUid != null){
               info.setSuccess(false);
               info.setMsg("学号已存在");
               writeValue(info,response);
               return;
           }
       }

        userService.update(user);
        request.getSession().setAttribute("User",user);
        info.setSuccess(true);
        info.setMsg("修改成功");
        writeValue(info,response);
    }
    public void emailSendServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        User_password user = (User_password) request.getSession().getAttribute("User");
        String id = request.getParameter("visit_id");
        if(user.getId() != Integer.parseInt(id)){
            info.setSuccess(false);
            info.setMsg("没有权限进行操作");
            writeValue(info,response);
            return;
        }
        User user1 = userService.find(id);
        if(user1.isStatus()){
            info.setMsg("您已激活，请不要重复激活");
            info.setSuccess(false);
            writeValue(info,response);
            return;
        }
        String uuid = UuidUtil.getUuid();
        String code = uuid.substring(0,6);
        String text = "您好，您的激活码为："+ code+"。\n请及时进行激活。";
        MailUtils.sendMail(user1.getEmail(),text,"激活码");
        System.out.println(text);
        request.getSession().setAttribute("realCode",code);
        info.setSuccess(true);
        info.setMsg("发送成功");
        writeValue(info,response);
    }
    public void emailActiveServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String inputCode = request.getParameter("code");
        String realCode = (String) request.getSession().getAttribute("realCode");
        request.getSession().removeAttribute("realCode");
        if(!realCode.equals(inputCode)){
            info.setSuccess(false);
            info.setMsg("验证码错误");
            writeValue(info,response);
            return;
        }
        User_password user = (User_password) request.getSession().getAttribute("User");
        String id = request.getParameter("visit_id");
        if(user.getId()!=Integer.parseInt(id)){
            info.setSuccess(false);
            info.setMsg("您没有权限进行操作");
            return;
        }

        user.setStatus(true);
        userService.update(user);
        info.setMsg("验证成功");
        info.setSuccess(true);
        writeValue(info,response);
    }
    public void BugServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String text = request.getParameter("text");
        Date date = new Date();
        BugInfo bugInfo = new BugInfo();
        bugInfo.setTime(date);
        bugInfo.setText(text);
        new UserDaoImpl().updateBug(bugInfo);
        info.setSuccess(true);
        info.setMsg("提交成功");
        writeValue(info,response);
    }
    public void rantingChangeServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        RantingChangeInfo rantingChangeInfo = new RantingChangeInfo();
        String id = request.getParameter("id");

        List<Integer> contestByUserId = userService.findContestByUserId(id);

        List<String> strContest = new ArrayList<String>();

        for(int contest : contestByUserId) strContest.add("#"+contest);

        rantingChangeInfo.setContest(strContest);

        List<Integer> rantingByUserId = userService.findRantingByUserId(id);
        rantingChangeInfo.setRanting(rantingByUserId);

        rantingChangeInfo.setSuccess(true);
        writeValue(rantingChangeInfo,response);
    }
}
