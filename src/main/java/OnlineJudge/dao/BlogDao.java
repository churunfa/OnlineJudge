package OnlineJudge.dao;

import OnlineJudge.domain.Solution;

import java.util.List;

public interface BlogDao {
    List<Solution> findBlogByPid(int pid);
    Solution findBlogById(int id);
    int addBlog(int master,int pid,String path,String title);
    void updateBlog(int id,String title);
    void summitBlog(int id);
    int findLove(int uid,int id);
    int checkLove(int uid,int id);
    void addLove(int uid,int id);
    void updateLove(int uid,int id,int flag);
    void changeLove(int id,int d);
    void delBlog(int id);
    void delLove(int id);
    void delBlogAndLove(int id);
}
