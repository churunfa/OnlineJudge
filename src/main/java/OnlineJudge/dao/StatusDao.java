package OnlineJudge.dao;

import OnlineJudge.domain.Status;

import java.sql.SQLException;
import java.util.List;

public interface StatusDao {
    List<Status> findAllStatus();
    List<Status> findStatusByLanguage(String language);
    List<Status> findStatusByUid(int uid);
    List<Status> findStatusByPid(int pid);
    List<Status> findAcStatusByPid(int pid);
    List<Status> findStatusByPidAndUid(int pid,int uid);
    int findCountByUidAndStatus(int id,String status);
    int findSignCountByUid(int id);
    int findContestCountByUid(int id);
    int checkAcOrNoByPidAndUid(int pid,int uid);
    int checkSub(int pid,int uid);
    void addStatus(Status sta);
    List<Integer> findUidByPid(int pid);
    void reTest(int staid) throws SQLException;
}
