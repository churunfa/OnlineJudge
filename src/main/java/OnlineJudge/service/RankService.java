package OnlineJudge.service;

import OnlineJudge.domain.PageBean;
import OnlineJudge.domain.RankInfo;
import OnlineJudge.domain.StatusInfo;

public interface RankService {
    PageBean<RankInfo> findRanks(int type,int pg,int size);
}
