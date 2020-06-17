package OnlineJudge.domain;

public class RankInfo {
    private User user;
    private int rank;
    private int ac_sum;
    private int sign_sum;
    private int con_sum;
    private int ranting;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getAc_sum() {
        return ac_sum;
    }

    public void setAc_sum(int ac_sum) {
        this.ac_sum = ac_sum;
    }

    public int getSign_sum() {
        return sign_sum;
    }

    public void setSign_sum(int sign_sum) {
        this.sign_sum = sign_sum;
    }

    public int getCon_sum() {
        return con_sum;
    }

    public void setCon_sum(int con_sum) {
        this.con_sum = con_sum;
    }

    public int getRanting() {
        return ranting;
    }

    public void setRanting(int ranting) {
        this.ranting = ranting;
    }
}
