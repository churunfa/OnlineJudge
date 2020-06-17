package OnlineJudge.dao;

import OnlineJudge.domain.DanMu;

import java.util.List;

public interface WaitDao {
    List<DanMu> findDanMu(int cid, int st);
    void addDanMu(int cid,int uid,String msg);
}
