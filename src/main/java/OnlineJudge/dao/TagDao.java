package OnlineJudge.dao;

import java.util.List;

public interface TagDao {
    int addTag(String name);
    int findTag(String name);
    String findTag(int tid);
    int findTag(int pid,int tid);
    List<Integer> findAllTag(int pid);
}
