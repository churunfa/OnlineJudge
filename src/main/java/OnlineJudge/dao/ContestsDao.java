package OnlineJudge.dao;

import OnlineJudge.domain.Contest;
import OnlineJudge.domain.ContestRank;
import OnlineJudge.domain.ContestRankInfo;
import OnlineJudge.domain.Problem;
import com.google.protobuf.Internal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ContestsDao {
    List<Contest> findAllContest();
    List<Integer> findUserByCid(int cid);
    Contest findContestByCid(int cid);
    void sign(int uid,int cid);
    int checkSign(int uid,int cid);
    int addContest(Contest contest);
    void updateContest(Contest contest);
    List<Problem> findProblemByCid(int cid);
    void updateSum(int cid);
}
