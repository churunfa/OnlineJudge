package OnlineJudge.dao;

import OnlineJudge.domain.*;

import java.util.List;

public interface UserDao {
    /**
     * 通过user id登录
     * @param user
     * @return
     */
    User_password findByUidAndPassword(String uid,String password);

    /**
     * 通过Email登录
     * @param email
     * @return
     */
    User_password findByEmailAndPassword(String email,String password);

    /**
     * 通过id查询
     * @param Uid
     * @return
     */
     boolean findByUid(String Uid);

    /**
     * 通过Email查询
     * @param email
     * @return
     */
     boolean findByEmail(String email);

    /**
     * 插入注册信息
     * @param user
     */
    void save(User_password user);

    /**
     * 更新登录时间
     */
    void update_date(int id);

    /**
     * 通过id查找用户信息
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 更新用户信息
     * @param user
     */
    void update_User(User_password user);
    void update_User(User user);

    /**
     * 通过uid查找用户信息
     * @param uid
     * @return
     */
    User findByUId(String uid);

    /**
     * 查询错误信息
     * @return
     */
    List<BugInfo> fineBug();

    /**
     * 插入bug信息
     */
    void updateBug(BugInfo bug);

    /**
     * 通过用户id查找所有提交记录
     * @param id
     * @return
     */
    List<Status> findStatusById(int id);

    /**
     * 通过题目id查找题目
     * @param id
     * @return
     */
    Problem findProblemById(int id);

    /**
     * 通过用户id查询某种状态的提交记录
     * @param id
     * @return
     */
    List<Status> findStatusByStaAndId(String sta,int id);
    List<Status> findNotStatusByStaAndId(String sta,int id);

    List<Status> findStatusByPid(String sta,int pid);
    List<Status> findNotStatusByPid(String sta,int pid);

    int findStatusByPidCount(String sta,int pid);
    int findNotStatusByPidCount(String sta,int pid);

    int findAllStatusCountByPid(int pid);

    int findContestByMaster(int id);

    /**
     * 从contest_sign_up中查询用户参加的所有比赛
     * @param id
     * @return
     */
    List<Integer> findContestByUserId(int id);

    /**
     * 从contest_rank中查询ranting的变化
     * @param id
     * @return
     */
    List<Integer> findRantingByUserId(int id);

    List<Contest> findNotEndContest();

    User findUserById(int id);

    /**
     * 查询所有比赛的前三名
     * @return
     */
    List<Contest_rank> findTreeRank();

    Contest findContestByCid(int id);

    Contest_User_info findContest_User_info(int contest_id,int uid);

    List<Problem> findAllProblem();
    List<Tag> findAllTag();
    List<Integer> findTagByPid(int pid);
    List<Integer> findProblemByTag(int id);
    List<Problem> fineProblemByStLen(int start,int len);
    int findProblemCount();

    Tag findTagByTId(int tid);

    int findProblemCountByTid(int tid);

    List<User> findAllUser();

    void setContest_rank(Contest_rank rank);
}
