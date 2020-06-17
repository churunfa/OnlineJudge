package OnlineJudge.service.impl;

import OnlineJudge.dao.UserDao;
import OnlineJudge.dao.impl.UserDaoImpl;
import OnlineJudge.domain.*;
import OnlineJudge.service.UserService;

import java.lang.reflect.Array;
import java.util.*;

public class UserServiceImpl implements UserService {
    private UserDao userDao=new UserDaoImpl();
    @Override
    public info register(User_password user) {
        info info = new info();
        boolean byUid = userDao.findByUid(user.getUid());

        if(byUid){
            info.setSuccess(false);
            info.setMsg("学号已存在");
            return info;
        }

        boolean byEmail = userDao.findByEmail(user.getEmail());

        if(byEmail){
            info.setSuccess(false);
            info.setMsg("邮箱已存在");
            return info;
        }

        userDao.save(user);

        info.setSuccess(true);
        info.setMsg("注册成功");

        return info;
    }

    @Override
    public boolean active(String email) {
        return false;
    }

    @Override
    public User_password login(User_password user) {
        User_password user1 = new User_password();
        if(user.getEmail() !=null ) user1 = userDao.findByEmailAndPassword(user.getEmail(),user.getPassword());
        else if(user.getUid()!=null) user1 = userDao.findByUidAndPassword(user.getUid(),user.getPassword());

        if(user1 != null) userDao.update_date(user1.getId());
        return user1;
    }

    @Override
    public User find(String id) {
        int int_id = Integer.parseInt(id);
        User byId = userDao.findById(int_id);
        return byId;
    }
    
    @Override
    public void update(User user) {
        userDao.update_User(user);
    }
    @Override
    public void update(User_password user) {
        userDao.update_User(user);
    }
    @Override
    public User findByUid(String uid) {
        User byUid = userDao.findByUId(uid);
        return byUid;
    }


    @Override
    public List<Problem> findStaById(String sta,String id) {
        List<Status> ACs = userDao.findStatusByStaAndId(sta, Integer.parseInt(id));
        HashSet<Integer> hs = new HashSet<>();
        for(Status AC:ACs) hs.add(AC.getProblem_id());

        List<Problem> problems = new ArrayList<>();

        Iterator<Integer> it = hs.iterator();

        while(it.hasNext()){
            Integer next = it.next();
            Problem problem = userDao.findProblemById(next);
            problems.add(problem);
        }
        return problems;
    }

    @Override
    public List<Problem> findNotStaById(String sta, String id) {
        List<Status> ACs = userDao.findNotStatusByStaAndId(sta, Integer.parseInt(id));
        HashSet<Integer> hs = new HashSet<Integer>();
        for(Status AC:ACs) hs.add(AC.getProblem_id());

        List<Problem> problems = new ArrayList<>();

        Iterator<Integer> it = hs.iterator();

        while(it.hasNext()){
            Integer next = it.next();
            Problem problem = userDao.findProblemById(next);
            problems.add(problem);
        }
        return problems;
    }

    @Override
    public int findContestByMaster(String id) {
        return userDao.findContestByMaster(Integer.parseInt(id));
    }

    @Override
    public List<Integer> findContestByUserId(String id) {
        return userDao.findContestByUserId(Integer.parseInt(id));
    }

    @Override
    public List<Integer> findRantingByUserId(String id) {
        return userDao.findRantingByUserId(Integer.parseInt(id));
    }

    @Override
    public List<ContestAndMaster> findNotEndContestAndMaster() {
        List<Contest> contests = userDao.findNotEndContest();
        List<ContestAndMaster> ans = new ArrayList<ContestAndMaster>();
        for(Contest contest:contests){
            ContestAndMaster contestAndMaster = new ContestAndMaster();
            contestAndMaster.setContest(contest);
            User userById = userDao.findUserById(contest.getMaster());
            contestAndMaster.setMaster(userById);
            ans.add(contestAndMaster);
        }
        return ans;
    }

    @Override
    public List<Top3> findTop3() {
        List<Top3> tops = new ArrayList<Top3>();
        List<Contest_rank> treeRank = userDao.findTreeRank();

        int maxn = 0;
        for(Contest_rank info : treeRank) maxn=Math.max(maxn,info.getContest_id());

        for(int i=0;i < maxn; i++) tops.add(new Top3());

        for(Contest_rank info : treeRank){
            Contest contest = userDao.findContestByCid(info.getContest_id());
            User user = userDao.findUserById(info.getUid());
            user.setRanting(info.getRanting());

            Top3 top = tops.get(info.getContest_id()-1);
            top.setContest(contest);
            if(info.getUser_rank()==1) top.setTop1(user);
            else if(info.getUser_rank()==2) top.setTop2(user);
            else if(info.getUser_rank()==3) top.setTop3(user);

            tops.set(info.getContest_id()-1,top);
        }

        List<Top3> tops_ans = new ArrayList<Top3>();
        int sum = 0;
        for(Top3 top:tops){
            if(top.getContest() == null) continue;
            tops_ans.add(top);
            sum++;
            if(sum >=10) break;
        }


        return tops_ans;
    }

    @Override
    public PageBean findPageBeanByPage(int nowPage,int pageSize) {
        PageBean<ProblemInfo> page = new PageBean<ProblemInfo>();
        page.setTotalCount(userDao.findProblemCount());
        page.setPageSize(pageSize);
        page.setCurrentPage(nowPage);

        int tot = page.getTotalCount()/page.getPageSize();

        if( page.getTotalCount()%page.getPageSize() != 0 ) tot++;

        page.setTotalPage(tot);

        List<Problem> allProblem = userDao.findAllProblem();

        List<ProblemInfo> pro = new ArrayList<>();

        Collections.sort(allProblem,new Comparator<Problem>() {
            @Override
            public int compare(Problem p1, Problem p2) {
                if(p1.getContest_id()>p2.getContest_id()) return -1;
                else if(p1.getContest_id()<p2.getContest_id()) return 1;
                else if(p1.getType().compareTo(p2.getType())>0) return 1;
                else if(p1.getType().compareTo(p2.getType())<0) return -1;
                else return 0;
            }
        });

        for(int i=0;i<Math.min(allProblem.size(),pageSize);i++){
            ProblemInfo problemInfo = new ProblemInfo();
            int in = (nowPage-1)*pageSize+i;
            if(in > page.getTotalCount()) break;
            Problem p = allProblem.get(in);

            problemInfo.setProblem(p);

            int acs = userDao.findStatusByPidCount("答案正确", p.getPid());
            int sum = userDao.findAllStatusCountByPid(p.getPid());

            problemInfo.setAc(acs);
            problemInfo.setSum(sum);
            if(sum == 0) problemInfo.setRate(0);
            else problemInfo.setRate((double)acs/sum*100);

            List<Integer> tagIds = userDao.findTagByPid(p.getPid());

            List<Tag> tags = new ArrayList<Tag>();
            for(int tagId : tagIds) tags.add(userDao.findTagByTId(tagId));

//            for(Tag tag:tags) System.out.println(tag);
            problemInfo.setTags(tags);
            pro.add(problemInfo);
        }
        page.setList(pro);
        return page;
    }

    @Override
    public PageBean findPageBeanByTag(int nowPage, int pageSize, int tag_id) {
        PageBean<ProblemInfo> page = new PageBean<ProblemInfo>();
        page.setTotalCount(userDao.findProblemCountByTid(tag_id));
        page.setPageSize(pageSize);
        page.setCurrentPage(nowPage);

        int tot = page.getTotalCount()/page.getPageSize();

        if( page.getTotalCount()%page.getPageSize() != 0 ) tot++;

        page.setTotalPage(tot);

        List<Integer> problems = userDao.findProblemByTag(tag_id);

        List<Problem> allProblem = new ArrayList<>();

        for(int problem:problems) {
            Problem pro = userDao.findProblemById(problem);
            if(!pro.isIs_show()) continue;
            allProblem.add(pro);
        }

        List<ProblemInfo> pro = new ArrayList<>();

        Collections.sort(allProblem,new Comparator<Problem>() {
            @Override
            public int compare(Problem p1, Problem p2) {
                if(p1.getContest_id()>p2.getContest_id()) return -1;
                else if(p1.getContest_id()<p2.getContest_id()) return 1;
                else if(p1.getType().compareTo(p2.getType())>0) return 1;
                else if(p1.getType().compareTo(p2.getType())<0) return -1;
                else return 0;
            }
        });

        for(int i=0;i<Math.min(allProblem.size(),pageSize);i++){
            ProblemInfo problemInfo = new ProblemInfo();
            int in = (nowPage-1)*pageSize+i;
            if(in > page.getTotalCount()) break;
            Problem p = allProblem.get(in);

            problemInfo.setProblem(p);

            int acs = userDao.findStatusByPidCount("答案正确", p.getPid());
            int sum = userDao.findAllStatusCountByPid(p.getPid());

            problemInfo.setAc(acs);
            problemInfo.setSum(sum);
            if(sum == 0) problemInfo.setRate(0);
            else problemInfo.setRate((double)acs/sum*100);

            List<Integer> tagIds = userDao.findTagByPid(p.getPid());

            List<Tag> tags = new ArrayList<Tag>();
            for(int tagId : tagIds) tags.add(userDao.findTagByTId(tagId));

//            for(Tag tag:tags) System.out.println(tag);
            problemInfo.setTags(tags);
            pro.add(problemInfo);
        }
        page.setList(pro);
        return page;
    }

}
