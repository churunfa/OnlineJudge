package OnlineJudge.service.impl;

import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.StatusDaoImpl;
import OnlineJudge.dao.impl.UserDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.service.StatusService;
import com.mysql.cj.PreparedQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class StatusServiceImpl implements StatusService {
    StatusDaoImpl statusDao = new StatusDaoImpl();
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    UserDaoImpl userDao = new UserDaoImpl();

    @Override
    public PageBean<StatusInfo> findStatus(PageInfo info) {
        PageBean<StatusInfo> page = new PageBean<StatusInfo>();
        int pg = info.getPg();
        int size = info.getSize();
        int pid = info.getPid();
        String sta = info.getSta();
        String title = info.getTitle();
        String type = info.getType();
        String name = info.getUname();

        page.setPageSize(size);
        page.setCurrentPage(pg);

        ContestsDaoImpl contestsDao = new ContestsDaoImpl();
//        boolean flag = false;
//        Contest contest = null;
//        if(info.getCid() != 0) contest = contestsDao.findContestByCid(info.getCid());
//
//        if(contest != null){
//            if(contest.getStart_time().getTime() > new Date().getTime()) flag = true;
//            if(contest.getMaster() == info.getSign_uid()) flag = true;
//        }
//
        List<Status> allStatus = statusDao.findAllStatus();
        List<StatusInfo> allSta = new ArrayList<>();
        if(allStatus != null){
            for(Status status:allStatus){
                Problem problem = problemDao.findProblemByPid(status.getProblem_id());
                if(info.getCid() != 0 &&info.getCid() != problem.getContest_id()) continue;

                boolean flag = false;
                Contest contest = null;
                if(problem.getContest_id() > 0) contest = contestsDao.findContestByCid(problem.getContest_id());

                if(contest != null){
                    if(contest.getStart_time().getTime() < new Date().getTime()) flag = true;
                    if(contest.getMaster() == info.getSign_uid()) flag = true;
                }else flag = true;

                if(!flag) continue;

                StatusInfo statusInfo = new StatusInfo();
                statusInfo.setProblem(problem);
                statusInfo.setUser(userDao.findById(status.getUid()));
                statusInfo.setStatus(status);
                allSta.add(statusInfo);
            }
        }

        String patTitle = ".*?"+title + ".*?";
        String patName = ".*?"+name + ".*?";

        List<StatusInfo> filterSta = new ArrayList<>();


        if(allSta != null){
            for(StatusInfo status:allSta){
                if(sta != null && !sta.equals(status.getStatus().getStatus())) continue;
                if(type !=null && !type.equals(status.getStatus().getCode_type())) continue;

                if(title != null && !status.getProblem().getTitle().matches(patTitle)) continue;
                if(name != null && !status.getUser().getName().matches(patName)) continue;

                if(pid != 0 && status.getProblem().getPid() != pid) continue;
                filterSta.add(status);
            }
        }

        page.setTotalCount(filterSta.size());

        int tot = 0;
        if(page.getPageSize() == 0) tot=0;
        else {
            tot = page.getTotalCount()/page.getPageSize();
            if(page.getTotalCount()%page.getPageSize() != 0) tot++;
        }
        page.setTotalPage(tot);

        List<StatusInfo> finalSta = new ArrayList<>();
        for(int i=0;i<size;i++){
            int in = (pg-1)*size+i;
            if(in >= filterSta.size()) break;
            finalSta.add(filterSta.get(in));
        }

        page.setList(finalSta);
        return page;
    }
}
