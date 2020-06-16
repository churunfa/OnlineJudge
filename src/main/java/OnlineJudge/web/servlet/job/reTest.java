package OnlineJudge.web.servlet.job;

import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.StatusDaoImpl;
import OnlineJudge.domain.Contest;
import OnlineJudge.domain.Problem;
import OnlineJudge.domain.Standard_code;
import OnlineJudge.domain.Status;
import org.quartz.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
public class reTest implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //创建工作详情
        JobDetail jobDetail = context.getJobDetail();
        String name = jobDetail.getKey().getName();//任务名
        String group = jobDetail.getKey().getGroup();//任务group
        String cid = jobDetail.getJobDataMap().getString("cid");//任务中的数据
        ContestsDaoImpl contestsDao = new ContestsDaoImpl();
        ProblemDaoImpl problemDao = new ProblemDaoImpl();
        if(cid == null) return;
        Contest contest = contestsDao.findContestByCid(Integer.parseInt(cid));
        if(contest == null) return;

        List<Integer> pids = problemDao.findPidByCid(Integer.parseInt(cid));
        StatusDaoImpl statusDao = new StatusDaoImpl();

        System.out.println(new Date()+"正在执行"+group+"的任务任务"+name);

        for(int pid:pids){
            Standard_code code = problemDao.getStandard_code(pid);
            if(code == null || code.getCode().length() == 0) continue;
            List<Status> acStatus = statusDao.findAcStatusByPid(pid);
            for(Status sta:acStatus){
                try {
                    statusDao.reTest(sta.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        JobKey jobKey = new JobKey(name,group);
        try {
            context.getScheduler().deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
