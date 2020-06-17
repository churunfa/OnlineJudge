package OnlineJudge.domain;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;

public class QueueInfo {
    private int solution_id;
    private int problem_id;
    private String user_id;
    private String nick;
    private int time;
    private int memory;
    private Date in_date;
    private int result;
    private int language;
    private String ip;
    private int contest_id;
    private int valid;
    private int num;
    private int code_length;
    private Timestamp judgetime;
    private BigDecimal pass_rate;
    private int lint_error;
    private String judger;

    public int getSolution_id() {
        return solution_id;
    }

    public void setSolution_id(int solution_id) {
        this.solution_id = solution_id;
    }

    public int getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(int problem_id) {
        this.problem_id = problem_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public Date getIn_date() {
        return in_date;
    }

    public void setIn_date(Date in_date) {
        this.in_date = in_date;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getContest_id() {
        return contest_id;
    }

    public void setContest_id(int contest_id) {
        this.contest_id = contest_id;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCode_length() {
        return code_length;
    }

    public void setCode_length(int code_length) {
        this.code_length = code_length;
    }

    public Timestamp getJudgetime() {
        return judgetime;
    }

    public void setJudgetime(Timestamp judgetime) {
        this.judgetime = judgetime;
    }

    public int getLint_error() {
        return lint_error;
    }

    public void setLint_error(int lint_error) {
        this.lint_error = lint_error;
    }

    public String getJudger() {
        return judger;
    }

    public void setJudger(String judger) {
        this.judger = judger;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public BigDecimal getPass_rate() {
        return pass_rate;
    }

    public void setPass_rate(BigDecimal pass_rate) {
        this.pass_rate = pass_rate;
    }
}
