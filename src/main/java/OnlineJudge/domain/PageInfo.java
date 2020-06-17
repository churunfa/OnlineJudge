package OnlineJudge.domain;

public class PageInfo {
    //pg:当前页码
    //size:每页显示条数
    //uname:作者姓名
    //title:题目
    //type:语言
    //sta:状态
    //pid
    //cid
    private int pg;
    private int size;
    private String uname;
    private String title;
    private String type;
    private String sta;
    private int pid;
    private int cid;
    private int sign_uid;

    public int getPg() {
        return pg;
    }

    public void setPg(int pg) {
        this.pg = pg;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getSign_uid() {
        return sign_uid;
    }

    public void setSign_uid(int sign_uid) {
        this.sign_uid = sign_uid;
    }
}
