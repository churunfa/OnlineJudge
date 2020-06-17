package OnlineJudge.domain;

public class Contest_User_info {
    private int contest_id;
    private int uid;
    private int penalty;
    private int ac_sum;

    public int getContest_id() {
        return contest_id;
    }

    public void setContest_id(int contest_id) {
        this.contest_id = contest_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getAc_sum() {
        return ac_sum;
    }

    public void setAc_sum(int ac_sum) {
        this.ac_sum = ac_sum;
    }
}
