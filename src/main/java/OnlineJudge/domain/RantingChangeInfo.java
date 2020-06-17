package OnlineJudge.domain;

import java.util.List;

public class RantingChangeInfo {
    private boolean success;
    private List<String> contest;
    private List<Integer> ranting;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getContest() {
        return contest;
    }

    public void setContest(List<String> contest) {
        this.contest = contest;
    }

    public List<Integer> getRanting() {
        return ranting;
    }

    public void setRanting(List<Integer> ranting) {
        this.ranting = ranting;
    }
}
