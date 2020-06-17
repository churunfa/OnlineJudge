package OnlineJudge.domain;

public class ContestInfo {
    private Contest contest;
    private User user;
    private String time;
    private int type;
    private int acSum;
    private String sta;

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAcSum() {
        return acSum;
    }

    public void setAcSum(int acSum) {
        this.acSum = acSum;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

}
