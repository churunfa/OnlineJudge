package OnlineJudge.service;

import OnlineJudge.domain.*;

import java.util.List;

public interface ContestsService {
    PageBean<ContestInfo> findContestInfo(int pg,int size,int uid);
    List<User> findContestSign(int id);
    List<ProblemInfo> findProblemByCid(int cid,int uid);
    List<ContestRank> findRank(int cid);
}
