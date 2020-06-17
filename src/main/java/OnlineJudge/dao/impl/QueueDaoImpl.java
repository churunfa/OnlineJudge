package OnlineJudge.dao.impl;

import OnlineJudge.dao.QueueDao;
import OnlineJudge.domain.*;
import OnlineJudge.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class QueueDaoImpl implements QueueDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int push(QueueInfo queue) {
        String sql = "insert into solution(problem_id,user_id,in_date,language,ip,contest_id,code_length) values(?,?,?,?,?,?,?);";
                                                //1      2       3       4       5    6           7
        template.update(sql,queue.getProblem_id(),queue.getUser_id(),queue.getIn_date(),queue.getLanguage(),queue.getIp(),queue.getContest_id(),queue.getCode_length());
        return template.queryForObject("SELECT LAST_INSERT_ID();",int.class);
    }

    @Override
    public void pushCode(source_code code) {
        String sql = "insert into source_code(solution_id,source) values(?,?);";
        template.update(sql,code.getSolution_id(),code.getSource());
    }

    @Override
    public void pushInput(custominput input) {
        String sql = "insert custominput(solution_id,input_text) values(?,?);";
        template.update(sql,input.getSolution_id(),input.getInput_text());
    }

    @Override
    public void commitInfo(QueueInfo queue, source_code code, Status sta) {
        Connection conn = null;

        try{
            conn = JDBCUtils.getConnection();
        }catch (Exception e){
        }

        try {
            conn.setAutoCommit(false);

                int solution_id = push(queue);
                code.setSolution_id(solution_id);
                sta.setId(solution_id);
                pushCode(code);
                StatusDaoImpl statusDao = new StatusDaoImpl();
                statusDao.addStatus(sta);
                conn.commit();

        } catch (SQLException throwables) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(throwables);
            throwables.printStackTrace();
        }
    }

    @Override
    public int insertInput(QueueInfo queue, source_code code, custominput input) {
        Connection conn =  null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);

            int solution_id = push(queue);

            code.setSolution_id(solution_id);
            input.setSolution_id(solution_id);

            pushCode(code);
            pushInput(input);

            conn.commit();

            return solution_id;

        } catch (SQLException throwables) {
            System.out.println(throwables);
            throwables.printStackTrace();
            return 0;
        }
    }

    @Override
    public int findResInfoBySid(int id) {
        int res = -1;
        try{
            String sql = "select result from solution where solution_id = ? limit 1;";
            res = template.queryForObject(sql, int.class,id);
        }catch (Exception e){

        }
        return res;
    }

    @Override
    public String findRuntimeInfo(int solution_id) {
        String sql = "select count(*) from runtimeinfo where solution_id = ?;";
        int count = template.queryForObject(sql, int.class, solution_id);
        if(count == 0) return null;
        sql = "select error from runtimeinfo where solution_id = ?;";
        return template.queryForObject(sql,String.class,solution_id);
    }

    @Override
    public String findCompileInfo(int solution_id) {
        String sql = "select count(*) from compileinfo where solution_id = ?;";
        int count = template.queryForObject(sql, int.class, solution_id);
        if(count == 0) return null;
        sql = "select error from compileinfo where solution_id = ?;";
        return template.queryForObject(sql,String.class,solution_id);
    }

    @Override
    public String findSolutionCode(int solution_id) {
        String ans = null;
        String sql = "select source from source_code where solution_id = ?;";
        try{
            ans = template.queryForObject(sql,String.class,solution_id);
        }catch (Exception e){return null;}
        return ans;
    }

    @Override
    public int findPid(int solution_id) {
        int count = 0;
        String sql = "select problem_id from solution where solution_id = ?;";
        try{
            count = template.queryForObject(sql,int.class,solution_id);
        }catch (Exception e){
            return 0;
        }
        return count;
    }

    @Override
    public List<Integer> findAllCustomTest() {
        String sql = "select solution_id from solution where problem_id = 0;";
        return template.queryForList(sql,int.class);

    }

    @Override
    public void remove(int solution_id) {
        Connection conn =  null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);

            String sql1 = "delete from solution where solution_id = ?;";
            String sql2 = "delete from runtimeinfo where solution_id = ?;";
            String sql3 = "delete from source_code where solution_id = ?;";
            String sql4 = "delete from compileinfo where solution_id = ?;";
            String sql5 = "delete from custominput where solution_id = ?;";

            template.update(sql1,solution_id);
            template.update(sql2,solution_id);
            template.update(sql3,solution_id);
            template.update(sql4,solution_id);
            template.update(sql5,solution_id);

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
