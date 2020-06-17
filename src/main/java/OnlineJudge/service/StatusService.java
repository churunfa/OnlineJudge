package OnlineJudge.service;

import OnlineJudge.domain.PageBean;
import OnlineJudge.domain.PageInfo;
import OnlineJudge.domain.StatusInfo;

public interface StatusService {
    PageBean<StatusInfo> findStatus(PageInfo info);
}
