package OnlineJudge.web.servlet.job;

import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.domain.Contest;
import OnlineJudge.domain.Problem;
import org.quartz.*;

import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
public class ShowProblem implements Job {
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

        List<Problem> problems = contestsDao.findProblemByCid(Integer.parseInt(cid));

        boolean flag = true;
        for(Problem problem:problems){
            if(!problem.isIs_show()){
                flag = false;
                problem.setIs_show(true);
                problemDao.updateProblem(problem);
            }
        }
        System.out.println(new Date()+"正在执行"+group+"的任务任务"+name);

        if(flag){
            JobKey jobKey = new JobKey(name,group);
            try {
                context.getScheduler().deleteJob(jobKey);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

}
