package OnlineJudge.service;

import OnlineJudge.domain.User;

import java.util.List;

public interface ConJob {
    void ProblemsShow(int cid);
    void ProblemsHidden(int cid);
    void ChangeRantingAndName(int cid);
    void ProblemsHank(int cid);
}
