package OnlineJudge.domain;

import java.util.Date;

public class ContestRankInfo {
    private boolean acFlag;
    private Status firstAC;
    private Status lastAC;
    private int sum;
    private long penalty;
    private boolean first;

    public boolean isAcFlag() {
        return acFlag;
    }

    public void setAcFlag(boolean acFlag) {
        this.acFlag = acFlag;
    }

    public Status getFirstAC() {
        return firstAC;
    }

    public void setFirstAC(Status firstAC) {
        this.firstAC = firstAC;
    }

    public Status getLastAC() {
        return lastAC;
    }

    public void setLastAC(Status lastAC) {
        this.lastAC = lastAC;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public long getPenalty() {
        return penalty;
    }

    public void setPenalty(long penalty) {
        this.penalty = penalty;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}
