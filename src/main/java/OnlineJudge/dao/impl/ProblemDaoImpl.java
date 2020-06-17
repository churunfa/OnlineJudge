package OnlineJudge.dao.impl;

import OnlineJudge.dao.ProblemDao;
import OnlineJudge.domain.Contest;
import OnlineJudge.domain.Problem;
import OnlineJudge.domain.Standard_code;
import OnlineJudge.domain.codeInfo;
import OnlineJudge.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class ProblemDaoImpl implements ProblemDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public List<Integer> findPidByTitle(String title) {
        title = "%"+title+"%";
        String sql = "select pid from problem where title like ?;";
        return template.queryForList(sql,Integer.class,title);
    }

    @Override
    public Problem findProblemByPid(int pid) {
        Problem problem = null;
        try {
            //定义sql
            String sql = "select * from problem where pid = ?;";
            //执行sql
            problem = template.queryForObject(sql, new BeanPropertyRowMapper<Problem>(Problem.class),pid);
        }catch (Exception e){
            System.out.println(e);
        }
        return problem;
    }

    @Override
    public List<Integer> findPidByCid(int cid) {
        String sql = "select pid from problem where contest_id = ?;";
        return template.queryForList(sql,Integer.class,cid);
    }

    @Override
    public String findPath(int pid) {
        String sql = "SELECT path FROM problem WHERE pid = ?;";
        return template.queryForObject(sql,String.class,pid);
    }

    @Override
    public int findFirstAC(int pid,int master) {
        String sql = "select uid from status where problem_id = ? and status = '答案正确' and uid != 1 and uid !=? limit 1;";
        return template.queryForObject(sql,Integer.class,pid,master);
    }

    @Override
    public int checkAC(int pid, int master) {
        String sql = "select count(*) from status where problem_id = ? and status = '答案正确' and uid != 1 and uid !=?;";
        int ans = template.queryForObject(sql,Integer.class,pid,master);
        return ans == 0 ? 0:1;
    }

    @Override
    public int insertProblem(Problem problem) {
        String sql = "insert into problem(" +
                "contest_id," +
                "title," +
                "type," +
                "source," +
                "is_show," +
                "master," +
                "ranting," +
                "path," +
                "time_limit," +
                "memory_limit,"+
                "spj) values(?,?,?,?,?,?,?,?,?,?,?);";
        template.update(sql,problem.getContest_id(),problem.getTitle(),
                problem.getType(),problem.getSource(),problem.isIs_show(),problem.getMaster(),
                problem.getRanting(),problem.getPath(),problem.getTime_limit(),problem.getMemory_limit(),problem.getIsspj());
        return template.queryForObject("SELECT LAST_INSERT_ID();",int.class);
    }

    @Override
    public void updateProblem(Problem problem) {
        if(problem.getContest_id() != 0){
            ContestsDaoImpl contestsDao = new ContestsDaoImpl();
            Contest contest = contestsDao.findContestByCid(problem.getContest_id());
            String source = "Round #"+problem.getContest_id()+" "+contest.getName();
            problem.setSource(source);
        }
        String sql = "update problem set " +
                "contest_id = ?," +
                "title = ?," +
                "type = ?," +
                "source = ?," +
                "is_show = ?," +
                "master = ?," +
                "ranting = ?," +
                "path = ?," +
                "time_limit = ?," +
                "memory_limit = ?," +
                "spj = ? where pid = ?;";
        template.update(sql,problem.getContest_id(),problem.getTitle(),
                problem.getType(),problem.getSource(),problem.isIs_show(),problem.getMaster(),
                problem.getRanting(),problem.getPath(),problem.getTime_limit(),problem.getMemory_limit(),problem.getIsspj(),
                problem.getPid());
    }

    @Override
    public void addLanguage(int pid, String language) {
        String sql = "insert into problem_language(pid,language) values(?,?);";
        template.update(sql,pid,language);
    }

    @Override
    public List<String> findLanguages(int pid) {
        List<String> languages = new ArrayList<>();

        String sql = "select language from problem_language where pid = ?;";

        return template.queryForList(sql,String.class,pid);
    }

    @Override
    public void removeLanguage(int pid) {
        String sql = "delete from problem_language where pid = ?;";
        template.update(sql,pid);
    }

    @Override
    public void removeTag(int pid, int tid) {
        String sql = "delete from problem_tag where pid = ? and tag_id = ?;";
        template.update(sql,pid,tid);
    }

    @Override
    public void removeTag(int pid) {
        String sql = "delete from problem_tag where pid = ?;";
        template.update(sql,pid);
    }

    @Override
    public void addTag(int pid, int tid) {
        String sql = "insert into problem_tag(pid,tag_id) values(?,?);";
        template.update(sql,pid,tid);
    }

    @Override
    public void delProblem(int pid,int cid) {
        String sql = "delete from problem where pid = ?;";
        template.update(sql,pid);

        sql = "UPDATE contest SET SUM = (SELECT COUNT(*) FROM problem WHERE contest_id = ?) WHERE id = ?;";
        template.update(sql,cid,cid);
    }

    @Override
    public void updateCode(int pid, int uid, String code,String language) {
        String sql = "select count(*) from user_code where pid = ? and uid = ?;";
        int count = template.queryForObject(sql, int.class, pid, uid);
        if(count == 0){
            sql = "insert into user_code(pid,uid,code,language) values(?,?,?,?);";
            template.update(sql,pid,uid,code,language);
        }else{
            sql = "update user_code set code = ? ,language = ? where pid = ? and uid = ?;";
            template.update(sql,code,language,pid,uid);
        }
    }

    @Override
    public codeInfo findCode(int pid, int uid) {
        String sql = "select count(*) from user_code where pid = ? and uid = ?;";
        int count = template.queryForObject(sql, int.class, pid, uid);
        if(count != 0){
            sql = "select code,language from user_code where pid = ? and uid = ?;";
            return template.queryForObject(sql,new BeanPropertyRowMapper<codeInfo>(codeInfo.class),pid,uid);
        }
        return null;
    }

    @Override
    public Standard_code getStandard_code(int pid) {
        Integer count = template.queryForObject("select count(*) from standard_code where pid = ?;", int.class, pid);
        if(count == 0) return null;
        String sql = "select * from standard_code where pid = ?;";
        Standard_code standard_code = template.queryForObject(sql, new BeanPropertyRowMapper<Standard_code>(Standard_code.class), pid);
        if(standard_code.getCode() == null) standard_code.setCode("");
        return standard_code;
    }

    @Override
    public void standard_codeAdd(int pid,String code,String language) {
        if(code.length() == 0) code = null;
        String sql = "insert into standard_code values(?,?,?);";
        template.update(sql,pid,code,language);
    }

    @Override
    public void standard_codeSet(int pid, String code,String language) {
        if(code.length() == 0) code = null;
        String sql = "update standard_code set code = ? where pid = ?;";
        template.update(sql,code,pid);
        sql = "update standard_code set language = ? where pid = ?;";
        template.update(sql,language,pid);
    }
}
