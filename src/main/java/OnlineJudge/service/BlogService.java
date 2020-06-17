package OnlineJudge.service;

import OnlineJudge.domain.SolutionInfo;

import java.util.List;

public interface BlogService {
    List<SolutionInfo> findSolutionInfoBy(int pid,int uid);
}
