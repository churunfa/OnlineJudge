package OnlineJudge.dao.impl;

import OnlineJudge.dao.WaitDao;
import OnlineJudge.domain.DanMu;
import OnlineJudge.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class WaitDaoImpl implements WaitDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<DanMu> findDanMu(int cid, int st) {
        String sql = "SELECT * FROM danmu WHERE cid = ? LIMIT ?,10;";
        return template.query(sql,new BeanPropertyRowMapper<>(DanMu.class),cid,st);
    }

    @Override
    public void addDanMu(int cid, int uid, String msg) {
        String sql = "insert into danmu(cid,uid,msg) values(?,?,?);";
        template.update(sql,cid,uid,msg);
    }
}
