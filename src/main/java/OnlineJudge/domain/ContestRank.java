package OnlineJudge.domain;

import java.util.HashMap;

public class ContestRank {
    private User user;
    private HashMap<Integer,ContestRankInfo> info;
    private long penalty;
    private int sum;
    private String rank;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HashMap<Integer, ContestRankInfo> getInfo() {
        return info;
    }

    public void setInfo(HashMap<Integer, ContestRankInfo> info) {
        this.info = info;
    }

    public long getPenalty() {
        return penalty;
    }

    public void setPenalty(long penalty) {
        this.penalty = penalty;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
