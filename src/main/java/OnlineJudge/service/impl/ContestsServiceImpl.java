package OnlineJudge.service.impl;

import OnlineJudge.dao.StatusDao;
import OnlineJudge.dao.impl.ContestsDaoImpl;
import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.StatusDaoImpl;
import OnlineJudge.dao.impl.UserDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.service.ContestsService;
import OnlineJudge.util.TimeSub;

import java.util.*;

public class ContestsServiceImpl implements ContestsService {
    ContestsDaoImpl contestsDao = new ContestsDaoImpl();
    UserDaoImpl userDao = new UserDaoImpl();
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    StatusDaoImpl statusDao = new StatusDaoImpl();

    @Override
    public PageBean<ContestInfo> findContestInfo(int pg, int size,int uid) {
        PageBean<ContestInfo> page = new PageBean<>();

        List<Contest> allContest = contestsDao.findAllContest();

        page.setPageSize(size);
        page.setTotalCount(allContest.size());
        page.setCurrentPage(pg);

        int tot = 0;

        if(page.getPageSize() !=0 ){
            tot = page.getTotalCount()/page.getPageSize();
            if(page.getTotalCount()%page.getPageSize() != 0) tot++;
        }

        page.setTotalPage(tot);

        List<ContestInfo> info = new ArrayList<>();

        for(int i=0;i<size;i++){
            int in = (pg-1)*size+i;
            if(in >= page.getTotalCount()) break;

            Contest contest = allContest.get(in);

            ContestInfo contestInfo = new ContestInfo();
            User master = userDao.findById(contest.getMaster());
            contestInfo.setContest(contest);
            contestInfo.setUser(master);
            contestInfo.setTime(TimeSub.Sub(contest.getEnd_time(),contest.getStart_time()));

            long st = contest.getStart_time().getTime();
            long ed = contest.getEnd_time().getTime();
            long now = new Date().getTime();
            String sta = "";
            int type = 0;

            if(st > now){
                type = 1;//未开始
                sta = "距开始还有" + TimeSub.Sub(contest.getStart_time(),new Date());
            }
            else if(ed > now ){
                type = 2;//未结束
                sta = "已开始" + TimeSub.Sub(new Date(),contest.getStart_time());
                sta = sta + "<br>距离结束还有" + TimeSub.Sub(contest.getEnd_time(),new Date());
            }
            else if(now < ed + 12*60*60*1000 ){
                type = 3;//hank未结束
                Date time = new Date(ed + 12 * 60 * 60 * 1000);
                sta = "距离hank结束还有" + TimeSub.Sub(time,new Date());
            }
            else{
                type = 4;
                sta = "已结束";
            }

            int sum = 0;
            if(uid != 0){
                List<Integer> pids = problemDao.findPidByCid(contest.getId());

                for(int pid:pids){
                    sum += statusDao.checkAcOrNoByPidAndUid(pid,uid);
                }
            }
            contestInfo.setSta(sta);
            contestInfo.setType(type);
            contestInfo.setAcSum(sum);

            info.add(contestInfo);
        }

        Collections.sort(info,new Comparator<ContestInfo>() {
            //正在进行、hank未结束、未开始、结束
            @Override
            public int compare(ContestInfo info1, ContestInfo info2) {
                if(info1.getType() == 1 ){ //未开始
                    if(info2.getType() == 1){
                        Long t1 = info1.getContest().getStart_time().getTime();
                        Long t2 = info2.getContest().getStart_time().getTime();
                        if(t1 > t2) return -1;
                        else return 1;
                    }
                    else if(info2.getType() == 2 || info2.getType() == 3) return 1;
                    return -1;
                }else if(info1.getType() == 2){ //未结束
                    if(info2.getType() == 2){
                        Long t1 = info1.getContest().getStart_time().getTime();
                        Long t2 = info2.getContest().getStart_time().getTime();
                        if(t1 > t2 ) return -1;
                        return 1;
                    }
                    return -1;
                }else if(info1.getType() == 3){ //hank未结束
                    if(info2.getType() == 2) return 1;
                    else if(info2.getType() == 3 ){
                        Long t1 = info1.getContest().getStart_time().getTime();
                        Long t2 = info2.getContest().getStart_time().getTime();
                        if(t1 > t2 ) return -1;
                        return 1;
                    }
                    return -1;
                }else if(info1.getType() == 4 ){ //结束
                    if(info2.getType() == 4){
                        Long t1 = info1.getContest().getStart_time().getTime();
                        Long t2 = info2.getContest().getStart_time().getTime();
                        if(t1 > t2 ) return -1;
                        return 1;
                    }
                    return 1;
                }
                return 0;
            }
        });

        page.setList(info);

        return page;
    }

    @Override
    public List<User> findContestSign(int id) {
        List<Integer> userByCid = contestsDao.findUserByCid(id);

        List<User> users = new ArrayList<>();
        for(int user :userByCid){
            User byId = userDao.findById(user);
            users.add(byId);
        }

        Collections.sort(users,new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                if(u1.getRanting() < u2.getRanting()) return 1;
                return -1;
            }
        });
        return users;
    }

    @Override
    public List<ProblemInfo> findProblemByCid(int cid,int uid) {
        List<Problem> problems = contestsDao.findProblemByCid(cid);
        List<ProblemInfo> proInfo = new ArrayList<>();

        for(Problem problem : problems){
            ProblemInfo problemInfo = new ProblemInfo();
            problemInfo.setSum(userDao.findAllStatusCountByPid(problem.getPid()));
            problemInfo.setAc(userDao.findStatusByPidCount("答案正确",problem.getPid()));
            if(problemInfo.getSum() == 0) problemInfo.setRate(0);
            else problemInfo.setRate((double)100*problemInfo.getAc()/problemInfo.getSum());

            int flag = statusDao.checkSub(problem.getPid(),uid);

            if(flag == 0) problemInfo.setPos(0);
            else{
                int flag2 = statusDao.checkAcOrNoByPidAndUid(problem.getPid(), uid);
                if(flag2 == 0) problemInfo.setPos(1);
                else problemInfo.setPos(2);
            }
            problemInfo.setProblem(problem);
            proInfo.add(problemInfo);
        }
        return proInfo;
    }

    @Override
    public List<ContestRank> findRank(int cid) {
        List<ContestRank> ans = new ArrayList<>();
        List<Integer> sign_uids = contestsDao.findUserByCid(cid);
        List<Integer> uids = new ArrayList<>();
        Contest contest = contestsDao.findContestByCid(cid);
        List<Problem> problems = contestsDao.findProblemByCid(cid);

        HashMap<Integer,Integer> book = new HashMap<Integer, Integer>();

        HashSet<Integer> uid_set = new HashSet<>();
        for(Problem problem:problems){
            List<Integer> ids = statusDao.findUidByPid(problem.getPid());
            if(ids != null){
                for(int id:ids) uid_set.add(id);
            }
        }
        for(int uid:sign_uids) uid_set.add(uid);

        for(int id:uid_set) uids.add(id);

        for(Problem problem:problems){

            List<Status> status = statusDao.findStatusByPid(problem.getPid());

            int flag = 0;
            int firstAC = 0;
            Long min_time = 1000000000000000000L;
            for(Status sta:status){
                if(sta.getSub_time().getTime()>contest.getEnd_time().getTime()||sta.getSub_time().getTime()<contest.getStart_time().getTime()) continue;
                if("编译错误".equals(sta.getStatus())) continue;
                if("答案正确".equals(sta.getStatus())){
                    if(sta.getSub_time().getTime()>min_time) continue;
                    flag = 1;
                    firstAC = sta.getUid();
                }
            }

//            int flag = problemDao.checkAC(problem.getPid(), problem.getMaster());
            if(flag != 0) {
                book.put(problem.getPid(),firstAC);
            }else{
                book.put(problem.getPid(),0);
            }
        }

        for(int uid:uids){
            ContestRank contestRank = new ContestRank();
            User user = userDao.findUserById(uid);
            contestRank.setUser(user);

            HashMap<Integer,ContestRankInfo> hashMap = new HashMap<>();

            int sum_ac = 0;
            int sum_penalty = 0;

            for(Problem problem:problems){
                ContestRankInfo contestRankInfo = new ContestRankInfo();

                List<Status> status = statusDao.findStatusByPidAndUid(problem.getPid(), uid);
                boolean fiFlag = false;
                Status fi = new Status();
                Status ed = new Status();

                int  sum = 0;
                for(Status sta:status){
                    if(sta.getSub_time().getTime()>contest.getEnd_time().getTime()||sta.getSub_time().getTime()<contest.getStart_time().getTime()) continue;
                    if("编译错误".equals(sta.getStatus())) continue;
                    sum ++;
                    if("答案正确".equals(sta.getStatus())){
                        if(!fiFlag){
                            fi = ed = sta;
                            fiFlag=true;
                        }else{
                            ed = sta;
                        }
                    }
                }

                long penalty = 0;
                if(fiFlag) penalty = (ed.getSub_time().getTime() - contest.getStart_time().getTime())/1000;
                if(fiFlag) penalty += (sum - 1)*20*60;

                contestRankInfo.setAcFlag(fiFlag);
                contestRankInfo.setFirstAC(fi);
                contestRankInfo.setLastAC(ed);
                contestRankInfo.setSum(sum);
                contestRankInfo.setPenalty(penalty);

                if(book.get(problem.getPid()) == uid){
                    contestRankInfo.setFirst(true);
                }else{
                    contestRankInfo.setFirst(false);
                }
                hashMap.put(problem.getPid(),contestRankInfo);
                if(fiFlag) sum_ac ++;
                sum_penalty+=penalty;
            }
            contestRank.setInfo(hashMap);
            contestRank.setSum(sum_ac);
            contestRank.setPenalty(sum_penalty);
            ans.add(contestRank);
        }

        Collections.sort(ans,new Comparator<ContestRank>() {
            @Override
            public int compare(ContestRank a, ContestRank b) {
                if(a.getSum() < b.getSum()) return 1;
                if(a.getSum() > b.getSum()) return -1;
                if(a.getPenalty() > b.getPenalty()) return 1;
                return -1;
            }
        });

        int now = 1;

        for(ContestRank us:ans){
            if(sign_uids.contains(us.getUser().getId())) us.setRank(""+now++);
            else us.setRank("*");
        }
        return ans;
    }
}
