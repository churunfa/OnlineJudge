package OnlineJudge.domain;

public class Problem {
    private int pid;
    private int contest_id;
    private String title;
    private String type;
    private String source;
    private boolean is_show;
    private int master;
    private int ranting;
    private String path;
    private int time_limit;
    private double memory_limit;
    private int isspj;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getContest_id() {
        return contest_id;
    }

    public void setContest_id(int contest_id) {
        this.contest_id = contest_id;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isIs_show() {
        return is_show;
    }

    public void setIs_show(boolean is_show) {
        this.is_show = is_show;
    }

    public int getMaster() {
        return master;
    }

    public void setMaster(int master) {
        this.master = master;
    }

    public int getRanting() {
        return ranting;
    }

    public void setRanting(int ranting) {
        this.ranting = ranting;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(int time_limit) {
        this.time_limit = time_limit;
    }

    public double getMemory_limit() {
        return memory_limit;
    }

    public void setMemory_limit(double memory_limit) {
        this.memory_limit = memory_limit;
    }

    public int getIsspj() {
        return isspj;
    }

    public void setIsspj(int isspj) {
        this.isspj = isspj;
    }
}
