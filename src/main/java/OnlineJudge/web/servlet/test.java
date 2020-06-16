package OnlineJudge.web.servlet;

import OnlineJudge.web.servlet.job.ShowProblem;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class test {
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        //创建调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1","group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().
                        withIntervalInSeconds(2).withRepeatCount(1))
                .build();
        JobDetail jobDetail = JobBuilder.newJob(ShowProblem.class)
                .withIdentity("job04", "group04")
                .usingJobData("data", "hello world")
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        Thread.sleep(4000);
        scheduler.shutdown();
    }
}
