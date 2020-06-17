package OnlineJudge.dao;

import OnlineJudge.domain.QueueInfo;
import OnlineJudge.domain.Status;
import OnlineJudge.domain.custominput;
import OnlineJudge.domain.source_code;

import java.sql.SQLException;
import java.util.List;

public interface QueueDao {
    int push(QueueInfo queue);
    void pushCode(source_code code);
    void pushInput(custominput input);
    void commitInfo(QueueInfo queue, source_code code, Status sta) throws SQLException;
    int insertInput(QueueInfo queue,source_code code,custominput input);
    int findResInfoBySid(int id);
    String findRuntimeInfo(int solution_id);
    String findCompileInfo(int solution_id);
    String findSolutionCode(int solution_id);
    int findPid(int solution_id);
    List<Integer> findAllCustomTest();
    void remove(int solution_id);
}
