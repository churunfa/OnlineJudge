package OnlineJudge.domain;

public class UserLoginMsg {
    private User_password user;
    private boolean success;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User_password getUser() {
        return user;
    }

    public void setUser(User_password user) {
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
