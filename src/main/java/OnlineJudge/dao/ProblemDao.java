package OnlineJudge.dao;

import OnlineJudge.domain.Problem;
import OnlineJudge.domain.Standard_code;

import java.util.List;

public interface ProblemDao {
    List<Integer> findPidByTitle(String title);
    Problem findProblemByPid(int pid);
    List<Integer> findPidByCid(int cid);
    String findPath(int pid);
    int findFirstAC(int pid,int master);
    int checkAC(int pid,int master);
    int insertProblem(Problem problem);
    void updateProblem(Problem problem);
    void addLanguage(int pid,String language);
    List<String> findLanguages(int pid);
    void removeLanguage(int pid);
    void removeTag(int pid,int tid);
    void removeTag(int pid);
    void addTag(int pid,int tid);
    void delProblem(int pid,int cid);
    void updateCode(int pid,int uid,String code);
    String findCode(int pid,int uid);
    Standard_code getStandard_code(int pid);
    void standard_codeAdd(int pid,String code,String language);
    void standard_codeSet(int pid,String code,String language);
}
