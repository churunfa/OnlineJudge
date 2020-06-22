package OnlineJudge.web.servlet.job;

import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.UserDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.service.impl.ContestsServiceImpl;
import OnlineJudge.util.UserChange;
import OnlineJudge.web.servlet.ContestServlet;
import org.quartz.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RantingChange implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //创建工作详情
        JobDetail jobDetail = context.getJobDetail();
        String name = jobDetail.getKey().getName();//任务名
        String group = jobDetail.getKey().getGroup();//任务group
        String cid = jobDetail.getJobDataMap().getString("cid");//任务中的数据

        ContestsDaoImpl contestsDao = new ContestsDaoImpl();
        ProblemDaoImpl problemDao = new ProblemDaoImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        ContestsServiceImpl contestsService = new ContestsServiceImpl();

        List<ContestRank> rank_ = contestsService.findRank(Integer.parseInt(cid));

        List<ContestRank> ranks = new ArrayList<ContestRank>();
        for(ContestRank rank : rank_) if(!"*".equals(rank.getRank())) ranks.add(rank);

        int tot = ranks.size();
        int endSum = 0;

        for(int i = ranks.size() - 1;i >= 0;i--){
            ContestRank now_rank = ranks.get(i);
            User user = ranks.get(i).getUser();
            int oldRank = user.getRanting();
            if(i == ranks.size() - 1){
                int dRank = -50;
                System.out.println(user.getName()+"rank:"+dRank);
                user.setRanting(Math.max(0,user.getRanting()+dRank));
                userDao.update_User(UserChange.changeNameLv(user));

                Contest_rank contest_rank = new Contest_rank();
                contest_rank.setContest_id(Integer.parseInt(cid));
                contest_rank.setRanting(user.getRanting());
                contest_rank.setUser_rank(i+1);
                contest_rank.setUid(user.getId());
                userDao.setContest_rank(contest_rank);

                Contest_User_info contest_user_info = new Contest_User_info();
                contest_user_info.setUid(user.getId());
                contest_user_info.setContest_id(Integer.parseInt(cid));
                contest_user_info.setPenalty((int)now_rank.getPenalty());
                contest_user_info.setAc_sum(now_rank.getSum());
                userDao.updateContest_user_info(contest_user_info);
            }
            else{
                int endRank = tot - i - 1;
                int dRank = 0;
                System.out.println("总rank:"+endSum +" 后面的总人数:"+ endRank);
                if(endSum % endRank == 0) dRank += ((endSum / endRank ) - user.getRanting()) * 2;
                else dRank += ((endSum / endRank + 1) - user.getRanting()) * 2;
                dRank += endRank * 4;
                System.out.println(user.getName()+"rank:"+dRank);
                user.setRanting(Math.max(0,user.getRanting()+dRank));
                userDao.update_User(UserChange.changeNameLv(user));

                Contest_rank contest_rank = new Contest_rank();
                contest_rank.setContest_id(Integer.parseInt(cid));
                contest_rank.setRanting(user.getRanting());
                contest_rank.setUser_rank(i+1);
                contest_rank.setUid(user.getId());
                userDao.setContest_rank(contest_rank);

                Contest_User_info contest_user_info = new Contest_User_info();
                contest_user_info.setUid(user.getId());
                contest_user_info.setContest_id(Integer.parseInt(cid));
                contest_user_info.setPenalty((int)now_rank.getPenalty());
                contest_user_info.setAc_sum(now_rank.getSum());
                userDao.updateContest_user_info(contest_user_info);
            }
            endSum += oldRank;
        }

        System.out.println(new Date()+"正在执行"+group+"的任务任务"+name);

        JobKey jobKey = new JobKey(name,group);
        try {
            context.getScheduler().deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
