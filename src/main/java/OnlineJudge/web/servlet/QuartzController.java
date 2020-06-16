package OnlineJudge.web.servlet;

import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.domain.Contest;
import OnlineJudge.domain.JobAndTrigger;
import OnlineJudge.domain.User_password;
import OnlineJudge.domain.info;
import OnlineJudge.util.CronUtils;
import OnlineJudge.web.servlet.job.ShowProblem;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/quartz")
public class QuartzController extends BaseServlet {
    @Autowired
    private Scheduler scheduler;

    ContestsDaoImpl contestsDao = new ContestsDaoImpl();

    @RequestMapping("/showProblem")
    public void ShowProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SchedulerException, ClassNotFoundException {
        String id = request.getParameter("id");

        if(id == null) return;
        Contest contest = contestsDao.findContestByCid(Integer.parseInt(id));
        if(contest == null) return;

        User_password user = (User_password) request.getSession().getAttribute("User");

        if(contest == null) return;
        if(!"root".equals(user.getPower()) && contest.getMaster() != user.getId()) return;

        Date end_time = contest.getEnd_time();
        String cron = CronUtils.getCron(end_time);

        System.out.println("showProblem"+cron);

        JobAndTrigger job = new JobAndTrigger();
        job.setJobName("showProblem");
        job.setJobGroup(id);
        job.setCronExpression(cron);
        job.setJobClassName("OnlineJudge.web.servlet.job.ShowProblem");
        JobDetail jobDetail
                = JobBuilder.newJob((Class<? extends ShowProblem>)Class.forName(job.getJobClassName()))
                .withIdentity(job.getJobName(),job.getJobGroup()).storeDurably(true)
                .usingJobData("cid",id)
                .build();
        CronTrigger cronTrigger = null;
        cronTrigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(),job.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression()))
                .build();
        scheduler.scheduleJob(jobDetail,cronTrigger);
        request.getRequestDispatcher("/quartz/reTest?id="+id).forward(request, response);
    }
    @RequestMapping("/reTest")
    public void reTest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SchedulerException, ClassNotFoundException {
        String id = request.getParameter("id");

        info info = new info();

        if(id == null) return;
        Contest contest = contestsDao.findContestByCid(Integer.parseInt(id));
        if(contest == null) return;

        Date end_time = contest.getEnd_time();
        end_time = new Date(end_time.getTime()+12*60*60*1000);
        String cron = CronUtils.getCron(end_time);

        System.out.println("reTest"+cron);

        JobAndTrigger job = new JobAndTrigger();
        job.setJobName("reTest");
        job.setJobGroup(id);
        job.setCronExpression(cron);

        job.setJobClassName("OnlineJudge.web.servlet.job.reTest");
        JobDetail jobDetail
                = JobBuilder.newJob((Class<? extends ShowProblem>)Class.forName(job.getJobClassName()))
                .withIdentity(job.getJobName(),job.getJobGroup()).storeDurably(true)
                .usingJobData("cid",id)
                .build();
        CronTrigger cronTrigger = null;
        cronTrigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(),job.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression()))
                .build();
        scheduler.scheduleJob(jobDetail,cronTrigger);
        request.getRequestDispatcher("/quartz/rantingChange?id="+id).forward(request, response);
    }
    @RequestMapping("/rantingChange")
    public void rantingChange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SchedulerException, ClassNotFoundException{
        String id = request.getParameter("id");

        info info = new info();

        if(id == null) return;
        Contest contest = contestsDao.findContestByCid(Integer.parseInt(id));
        if(contest == null) return;
        User_password user = (User_password) request.getSession().getAttribute("User");
        if(contest == null) return;
        if(!"root".equals(user.getPower()) && contest.getMaster() != user.getId()) return;

        if(!"div1".equals(contest.getType())&&!"div2".equals(contest.getType())&&!"div3".equals(contest.getType())) return;

        Date end_time = contest.getEnd_time();
        end_time = new Date(end_time.getTime()+12*60*60*1000+30*60*1000);
        String cron = CronUtils.getCron(end_time);

        System.out.println("rantingChange"+cron);

        JobAndTrigger job = new JobAndTrigger();
        job.setJobName("rantingChange");
        job.setJobGroup(id);
        job.setCronExpression(cron);

        job.setJobClassName("OnlineJudge.web.servlet.job.RantingChange");
        JobDetail jobDetail
                = JobBuilder.newJob((Class<? extends ShowProblem>)Class.forName(job.getJobClassName()))
                .withIdentity(job.getJobName(),job.getJobGroup()).storeDurably(true)
                .usingJobData("cid",id)
                .build();
        CronTrigger cronTrigger
                = TriggerBuilder.newTrigger().withIdentity(job.getJobName(),job.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression()))
                .build();
        scheduler.scheduleJob(jobDetail,cronTrigger);
        info.setSuccess(true);
        info.setMsg("创建成功");
        writeValue(info,response);
    }
    @RequestMapping("/remove")
    public void removeJob(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SchedulerException, ClassNotFoundException {

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String group = request.getParameter("group");

        User_password user = (User_password) request.getSession().getAttribute("User");
        Contest contest = contestsDao.findContestByCid(Integer.parseInt(id));

        if(contest == null) return;
        if(!"root".equals(user.getPower()) && contest.getMaster() != user.getId()) return;

        JobKey jobKey = new JobKey("showProblem",id);
        scheduler.deleteJob(jobKey);
        System.out.println("移除showProblem");

        jobKey = new JobKey("reTest",id);
        scheduler.deleteJob(jobKey);
        System.out.println("移除resTest...");

        jobKey = new JobKey("rantingChange",id);
        scheduler.deleteJob(jobKey);
        System.out.println("移除rantingChange...");
        System.out.println("移除成功-----------");
        request.getRequestDispatcher("/quartz/showProblem?id="+id).forward(request, response);
    }
    @RequestMapping("/removeDelJob")
    public void removeDelJob(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SchedulerException, ClassNotFoundException {
        User_password user = (User_password) request.getSession().getAttribute("User");
            if(!"root".equals(user.getPower())) return;

        JobKey jobKey = new JobKey("delCustomTest","delGroup");
        scheduler.deleteJob(jobKey);
        System.out.println("移除delJob");
        writeValue("移除成功",response);
    }
}
