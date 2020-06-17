package OnlineJudge.domain;

import java.util.List;

public class WaitInfo {
    private boolean success;
    private Long time;
    private List<DanMu> danMu;
    private List<User> users;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<DanMu> getDanMu() {
        return danMu;
    }

    public void setDanMu(List<DanMu> danMu) {
        this.danMu = danMu;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
