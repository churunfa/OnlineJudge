package OnlineJudge.dao.impl;

import OnlineJudge.dao.ContestsDao;
import OnlineJudge.domain.Contest;
import OnlineJudge.domain.Problem;
import OnlineJudge.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ContestsDaoImpl implements ContestsDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public List<Contest> findAllContest() {
        String sql = "SELECT * FROM contest ORDER BY id DESC;";
        return template.query(sql,new BeanPropertyRowMapper<>(Contest.class));
    }

    @Override
    public List<Integer> findUserByCid(int cid) {
        String sql = "SELECT uid FROM contest_sign_up where contest_id = ?;";
        return template.queryForList(sql,Integer.class,cid);
    }

    @Override
    public Contest findContestByCid(int cid) {
        String sql = "SELECT * FROM contest where id = ?;";
        return template.queryForObject(sql,new BeanPropertyRowMapper<>(Contest.class),cid);
    }

    @Override
    public void sign(int uid, int cid) {
        String sql = "INSERT INTO contest_sign_up(uid,contest_id) VALUES(?,?);";
        template.update(sql,uid,cid);
    }

    @Override
    public int checkSign(int uid, int cid) {
        String sql = "SELECT COUNT(*) FROM contest_sign_up WHERE uid = ? AND contest_id = ?;";
        return template.queryForObject(sql,Integer.class,uid,cid);
    }

    @Override
    public int addContest(Contest contest) {
        String sql = "insert into contest(name,master,start_time,end_time,type,notice) values(?,?,?,?,?,?);";
        template.update(sql,contest.getName(),contest.getMaster(),contest.getStart_time(),contest.getEnd_time(),contest.getType(),contest.getNotice());
        return template.queryForObject("SELECT LAST_INSERT_ID();",int.class);
    }

    @Override
    public void updateContest(Contest contest) {
        String sql = "update contest set name = ?,start_time = ?,end_time = ?,type = ?,notice = ? where id = ?;";
        template.update(sql,
                contest.getName(),
                contest.getStart_time(),
                contest.getEnd_time(),
                contest.getType(),
                contest.getNotice(),
                contest.getId());
    }

    @Override
    public List<Problem> findProblemByCid(int cid) {
        String sql = "select * from problem where contest_id = ?;";
        return template.query(sql,new BeanPropertyRowMapper<>(Problem.class),cid);
    }

    @Override
    public void updateSum(int cid) {
        String sql = "UPDATE contest SET SUM = (SELECT COUNT(*) FROM problem WHERE contest_id = ?) WHERE id = ?;";
        template.update(sql,cid,cid);
    }
}
