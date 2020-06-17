package OnlineJudge.service;

public interface hackService {
    boolean check(String code,String language,String in,String out) throws InterruptedException;
    String askRes(int solution_id);
    int checkEnd(int solution_id);
}
