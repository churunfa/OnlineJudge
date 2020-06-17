package OnlineJudge.domain;

import java.util.Date;

public class Status {
    private int id;
    private int problem_id;
    private String status;
    private boolean is_show;
    private int uid;
    private Date sub_time;
    private int run_time;
    private int memory;
    private float length;
    private String code_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(int problem_id) {
        this.problem_id = problem_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIs_show() {
        return is_show;
    }

    public void setIs_show(boolean is_show) {
        this.is_show = is_show;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getSub_time() {
        return sub_time;
    }

    public void setSub_time(Date sub_time) {
        this.sub_time = sub_time;
    }

    public int getRun_time() {
        return run_time;
    }

    public void setRun_time(int run_time) {
        this.run_time = run_time;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getCode_type() {
        return code_type;
    }

    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }
}
