package OnlineJudge.dao.impl;

import OnlineJudge.dao.TagDao;
import OnlineJudge.util.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class TagDaoImpl implements TagDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int addTag(String name) {
        String sql= "insert into tag(name) values(?);";
        template.update(sql,name);
        return template.queryForObject("SELECT LAST_INSERT_ID();",int.class);
    }

    @Override
    public int findTag(String name) {
        String sql = "select count(*) from tag where name = ?;";
        int sum = template.queryForObject(sql,int.class,name);
        if( sum !=0 ){
            sql = "select id from tag where name = ? limit 1;";
            sum = template.queryForObject(sql,int.class,name);
        }
        return sum;
    }

    @Override
    public String findTag(int tid) {
        String name = null;
        String sql = "select count(*) from tag where id = ?;";
        int sum = template.queryForObject(sql,int.class,tid);
        if( sum !=0 ){
            sql = "select name from tag where id = ? limit 1;";
            name = template.queryForObject(sql,String.class,tid);
        }
        return name;
    }

    @Override
    public int findTag(int pid, int tid) {
        String sql = "select count(*) from problem_tag where pid = ? and tag_id = ?;";
        return template.queryForObject(sql,int.class,pid,tid);
    }

    @Override
    public List<Integer> findAllTag(int pid) {
        List<String> tags = new ArrayList<>();
        String sql = "select tag_id from problem_tag where pid = ?;";
        return template.queryForList(sql,Integer.class,pid);
    }

}
