package OnlineJudge.dao.impl;

import OnlineJudge.dao.BlogDao;
import OnlineJudge.domain.Solution;
import OnlineJudge.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class BlogDaoImpl implements BlogDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public List<Solution> findBlogByPid(int pid) {
        String sql = "select * from blog where pid = ? order by id desc;";
        return template.query(sql,new BeanPropertyRowMapper<>(Solution.class),pid);
    }

    @Override
    public Solution findBlogById(int id) {
        String sql = "select * from blog where id = ?;";
        return template.queryForObject(sql,new BeanPropertyRowMapper<>(Solution.class),id);
    }

    @Override
    public int addBlog(int master, int pid, String path, String title) {

        String sql = "insert into blog(master,pid,path,title) " +
                " values(?,?,?,?);";
        template.update(sql,
                master,
                pid,
                path,
                title
        );
        sql = "select max(id) from blog;";
        return template.queryForObject(sql,Integer.class);
    }

    @Override
    public void updateBlog(int id,String title) {
        String sql = "update blog set title = ? where id = ?;";
        template.update(sql,title,id);
    }

    @Override
    public void summitBlog(int id) {
        String sql = "update blog set `show` = 1,create_time = ? where id = ?;";
        template.update(sql,new Date(),id);
    }

    @Override
    public int findLove(int uid, int id) {
        String sql = "select count(*) from blog_love where uid = ? and blog_id = ? and flag=1;";
        return template.queryForObject(sql,Integer.class,uid,id);
    }

    @Override
    public int checkLove(int uid, int id) {
        String sql = "select count(*) from blog_love where uid = ? and blog_id = ?;";
        return template.queryForObject(sql,Integer.class,uid,id);
    }

    @Override
    public void addLove(int uid, int id) {
        String sql = "insert into blog_love (uid,blog_id,flag) values(?,?,?);";
        template.update(sql,uid,id,1);
    }

    @Override
    public void updateLove(int uid, int id, int flag) {
        String sql = "update blog_love set flag = ? where uid = ? and blog_id = ?;";
        template.update(sql,flag,uid,id);
    }

    @Override
    public void changeLove(int id, int d) {
        String sql1 = "select count(*) from blog_love where blog_id = ? and flag = 1;";
        int sum = template.queryForObject(sql1, Integer.class, id);
        String sql = "update blog set love = ? where id = ?;";
        template.update(sql,sum,id);
    }

    @Override
    public void delBlog(int id) {
        String sql = "delete from blog where id=?;";
        template.update(sql,id);
    }

    @Override
    public void delLove(int id) {
        String sql = "delete from blog_love where blog_id=?;";
        template.update(sql,id);
    }

    @Override
    public void delBlogAndLove(int id) {
        String sql = "delete from blog where id=?;";
        template.update(sql,id);
        sql = "delete from blog_love where blog_id=?;";
        template.update(sql,id);
    }
}
