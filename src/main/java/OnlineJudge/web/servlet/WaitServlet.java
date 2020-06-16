package OnlineJudge.web.servlet;

import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.dao.impl.UserDaoImpl;
import OnlineJudge.dao.impl.WaitDaoImpl;
import OnlineJudge.domain.*;
import org.quartz.SchedulerException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/waitServlet/*")
public class WaitServlet extends BaseServlet {
    WaitDaoImpl waitDao = new WaitDaoImpl();
    UserDaoImpl userDao = new UserDaoImpl();
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    public void timeAndDanMu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        WaitInfo waitInfo = new WaitInfo();
        String id = request.getParameter("id");
        String sts = request.getParameter("st");
        int st = 0;

        if(sts != null ) st = Integer.parseInt(sts);

        if(id == null){
            request.getRequestDispatcher(request.getContextPath()+"/error").forward(request,response);
            return;
        }

        Contest contestByCid = contestsDao.findContestByCid(Integer.parseInt(id));

        List<DanMu> danMu = waitDao.findDanMu(Integer.parseInt(id), st);

        waitInfo.setTime(contestByCid.getStart_time().getTime());
        waitInfo.setSuccess(true);
        waitInfo.setDanMu(danMu);

        List<User> users = new ArrayList<>();

        for(DanMu danmu:danMu){
            int uid = danmu.getUid();
            User byId = userDao.findById(uid);
            byId.setUid("");
            users.add(byId);
        }

        waitInfo.setUsers(users);

        writeValue(waitInfo,response);
    }
    public void submitDanMu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        info info = new info();
        String msg = request.getParameter("msg");
        String id = request.getParameter("id");
        User_password user = (User_password) request.getSession().getAttribute("User");
        if(user == null){
            info.setMsg("请登录!");
            info.setSuccess(false);;
            writeValue(info,response);
        }
        try {
            waitDao.addDanMu(Integer.parseInt(id),user.getId(),msg);
            info.setSuccess(true);
            info.setMsg("发送成功");
            writeValue(info,response);
        }catch (Exception e){
            info.setSuccess(false);
            info.setMsg("服务器错误");
            writeValue(info,response);
        }

    }
}
