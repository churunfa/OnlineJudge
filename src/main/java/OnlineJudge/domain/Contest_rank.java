package OnlineJudge.domain;

public class Contest_rank {
    private int contest_id;
    private int uid;
    private int user_rank;
    private int ranting;

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

    public int getUser_rank() {
        return user_rank;
    }

    public void setUser_rank(int user_rank) {
        this.user_rank = user_rank;
    }

    public int getRanting() {
        return ranting;
    }

    public void setRanting(int ranting) {
        this.ranting = ranting;
    }
}
