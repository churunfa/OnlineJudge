package OnlineJudge.dao.impl;

import OnlineJudge.dao.StatusDao;
import OnlineJudge.domain.Status;
import OnlineJudge.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StatusDaoImpl implements StatusDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Status> findAllStatus() {
        String sql = "SELECT * FROM status ORDER BY id DESC;";
        return template.query(sql, new BeanPropertyRowMapper<Status>(Status.class));
    }

    @Override
    public List<Status> findStatusByLanguage(String language) {
        String sql = "select * from status where code_type = ? ORDER BY id DESC;";
        return template.query(sql,new BeanPropertyRowMapper<>(Status.class),language);
    }

    @Override
    public List<Status> findStatusByUid(int uid) {
        String sql = "select * from status where uid = ? ORDER BY id DESC;";
        return template.query(sql, new BeanPropertyRowMapper<Status>(Status.class),uid);
    }

    @Override
    public List<Status> findStatusByPid(int pid) {
        String sql = "select * from status where problem_id = ? ORDER BY id DESC;";
        return template.query(sql, new BeanPropertyRowMapper<Status>(Status.class),pid);
    }

    @Override
    public List<Status> findAcStatusByPid(int pid) {
        String sql = "select * from status where problem_id = ? and status = '答案正确';";
        return template.query(sql, new BeanPropertyRowMapper<Status>(Status.class),pid);
    }

    @Override
    public List<Status> findStatusByPidAndUid(int pid, int uid) {
        String sql = "select * from status where uid = ?  and problem_id = ?;";
        return template.query(sql, new BeanPropertyRowMapper<Status>(Status.class),uid,pid);
    }

    @Override
    public int findCountByUidAndStatus(int id, String status) {
        String sql = "SELECT COUNT(DISTINCT problem_id)  FROM status WHERE uid = ? AND status = ?;";
        return template.queryForObject(sql,int.class,id,status);
    }

    @Override
    public int findSignCountByUid(int id) {
        String sql = "select count(*) from contest_sign_up where uid = ?;";
        return template.queryForObject(sql,Integer.class,id);
    }

    @Override
    public int findContestCountByUid(int id) {
        String sql = "select count(*) from contest where master = ?;";
        return template.queryForObject(sql,Integer.class,id);
    }

    @Override
    public int checkAcOrNoByPidAndUid(int pid, int uid) {
        String sql = "SELECT count(*) FROM status WHERE problem_id = ? AND uid = ? AND status = '答案正确';";
        int ans =  template.queryForObject(sql,Integer.class,pid,uid);
        return ans == 0 ? 0 : 1;
    }

    @Override
    public int checkSub(int pid, int uid) {
        String sql = "SELECT count(*) FROM status WHERE problem_id = ? AND uid = ? LIMIT 1;";
        return template.queryForObject(sql,Integer.class,pid,uid);
    }

    @Override
    public void addStatus(Status sta) {
        String sql = "insert into status(id,problem_id,status,is_show,uid,sub_time,run_time,memory,length,code_type) values(?,?,?,?,?,?,?,?,?,?);";
        template.update(sql,sta.getId(),sta.getProblem_id(),sta.getStatus(),sta.isIs_show(),sta.getUid(),sta.getSub_time(),sta.getRun_time(),sta.getMemory(),sta.getLength(),sta.getCode_type());
    }

    @Override
    public List<Integer> findUidByPid(int pid) {
        String sql = "select distinct uid from status where problem_id = ?;";
        return template.queryForList(sql,Integer.class,pid);
    }

    @Override
    public void reTest(int staid) throws SQLException {
        String sql1 = "update status set status = '等待重测' where id = ?;";
        String sql2 = "update solution set result = 1 where solution_id = ?;";
        Connection conn = JDBCUtils.getConnection();
        try {
            conn.setAutoCommit(false);
            template.update(sql1,staid);
            template.update(sql2,staid);
            conn.commit();
        } catch (SQLException throwables) {
            conn.rollback();
            System.out.println(throwables);
            throwables.printStackTrace();
        }
    }
}
