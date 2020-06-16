package OnlineJudge.web.servlet.job;

import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.QueueDaoImpl;
import OnlineJudge.dao.impl.StatusDaoImpl;
import OnlineJudge.domain.Contest;
import OnlineJudge.domain.Standard_code;
import OnlineJudge.domain.Status;
import org.quartz.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
public class DelCustomTest implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        String name = jobDetail.getKey().getName();//任务名
        String group = jobDetail.getKey().getGroup();//任务group

        System.out.println(new Date()+":运行删除任务->"+"name:"+name+" group:"+group);

        QueueDaoImpl queueDao = new QueueDaoImpl();

        List<Integer> CustomTests = queueDao.findAllCustomTest();

        for(int x:CustomTests) {
            queueDao.remove(x);
            System.out.println("成功删除"+x);
        }

//        Scheduler scheduler = context.getScheduler();
//        JobKey jobKey = new JobKey(name,group);
//        try {
//            scheduler.deleteJob(jobKey);
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }

    }
}
