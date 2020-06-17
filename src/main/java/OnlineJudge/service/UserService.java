package OnlineJudge.service;

import OnlineJudge.domain.*;

import java.util.List;

public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
    info register(User_password user);

    /**
     * 激活邮箱
     * @param email
     * @return
     */
    boolean active(String email);

    /**
     * 登录方法
     * @param user
     * @return
     */
    User_password login(User_password user);

    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    User find(String id);

    /**
     * 更新用户信息
     * @param user
     */
    void update(User user);

    /**
     * 通过学号查找用户
     * @param uid
     * @return
     */
    User findByUid(String uid);

    /**
     * 通过状态和用户id查询记录
     * @param id
     * @return
     */
    List<Problem> findStaById(String sta,String id);
    /**
     * 通过状态和用户id查询记录
     * @param id
     * @return
     */
    List<Problem> findNotStaById(String sta,String id);

    int findContestByMaster(String id);

    List<Integer> findContestByUserId(String id);

    List<Integer> findRantingByUserId(String id);

    List<ContestAndMaster> findNotEndContestAndMaster();

    List<Top3> findTop3();

    PageBean findPageBeanByPage(int nowPage,int pageSize);

    PageBean findPageBeanByTag(int nowPage,int pageSize,int tag_id);
    void update(User_password user);

}
