package OnlineJudge.domain;

public class Top3 {
    Contest contest;
    User top1;
    User top2;
    User top3;

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public User getTop1() {
        return top1;
    }

    public void setTop1(User top1) {
        this.top1 = top1;
    }

    public User getTop2() {
        return top2;
    }

    public void setTop2(User top2) {
        this.top2 = top2;
    }

    public User getTop3() {
        return top3;
    }

    public void setTop3(User top3) {
        this.top3 = top3;
    }
}
