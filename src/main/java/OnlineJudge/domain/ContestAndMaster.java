package OnlineJudge.domain;

public class ContestAndMaster {
    Contest contest;
    User master;

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }
}
