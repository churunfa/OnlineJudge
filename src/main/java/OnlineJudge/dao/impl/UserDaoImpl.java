package OnlineJudge.dao.impl;

import OnlineJudge.dao.UserDao;
import OnlineJudge.domain.*;
import OnlineJudge.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDaoImpl implements UserDao {
    /**
     * 获取数据库连接池
     */
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public User_password findByUidAndPassword(String uid,String password) {
        User_password user=null;
        try {
            //定义sql
            String sql = "select * from user_info where uid = ? and password = ?;";
            //执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User_password>(User_password.class), uid,password);
        }catch (Exception e){
        }
        return user;
    }

    @Override
    public User_password findByEmailAndPassword(String email,String password) {
        User_password user=null;
        try {
            //定义sql
            String sql = "select * from user_info where email = ? and password = ?;";
            //执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User_password>(User_password.class), email,password);
        }catch (Exception e){
        }
        if(user != null) update_date(user.getId());
        return user;
    }

    @Override
    public boolean findByUid(String Uid) {
        int sum = 0;
        try {
            //定义sql
            String sql = "SELECT COUNT(*) FROM user_info WHERE uid = ?;";
            //执行sql
            sum = template.queryForObject(sql, int.class, Uid);
        }catch (Exception e){
        }
        if(sum == 0) return false;
        return true;
    }

    @Override
    public boolean findByEmail(String email) {
        int sum=0;
        try {
            //定义sql
            String sql = "select count(*) from user_info where email = ? ;";
            //执行sql
            sum = template.queryForObject(sql, int.class, email);
        }catch (Exception e){
        }
        if(sum == 0) return false;
        return true;
    }

    @Override
    public void save(User_password user) {

        String name = user.getName();
        name = "<span style='color: "+"#C9C9C9"+"'>"+name+"</span>";

        user.setName(name);

        String sql = "insert into user_info(uid,name,password,email) " +
                " values(?,?,?,?);";
        template.update(sql,
                user.getUid(),
                user.getName(),
                user.getPassword(),
                user.getEmail()
        );
    }

    @Override
    public void update_date(int id) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        "update pet set name='kk0' where name='kk1';"
        String sql = "update user_info set last_login = ? where id = ?;";
        template.update(sql,new Date(), id);
    }

    @Override
    public User findById(int id) {
        User user=null;
        try {
            //定义sql
            String sql = "select * from user_info where id = ?;";
            //执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), id);
        }catch (Exception e){
        }
        if(user != null) update_date(user.getId());
        return user;
    }

    @Override
    public void update_User(User_password user) {
        String sql = "update user_info set uid = ? ," +
                "email = ? ," +
                "password = ?," +
                "major = ? ," +
                "grade = ? ," +
                "name = ? ," +
                "sex = ? ," +
                "head_img = ? ," +
                "ranting = ? ," +
                "lv = ? ," +
                "status = ? ," +
                "last_login = ? ," +
                "power = ? ,"+
                "max_ranting = ? "+
                " where id = ?;";
        template.update(sql,
                user.getUid(),
                user.getEmail(),
                user.getPassword(),
                user.getMajor(),
                user.getGrade(),
                user.getName(),
                user.getSex(),
                user.getHead_img(),
                user.getRanting(),
                user.getLv(),
                user.isStatus(),
                user.getLast_login(),
                user.getPower(),
                user.getMax_ranting(),
                user.getId());
    }
    @Override
    public void update_User(User user) {
        String sql = "update user_info set uid = ? ," +
                "email = ? ," +
                "major = ? ," +
                "grade = ? ," +
                "name = ? ," +
                "sex = ? ," +
                "head_img = ? ," +
                "ranting = ? ," +
                "lv = ? ," +
                "status = ? ," +
                "last_login = ? ," +
                "power = ? ,"+
                "max_ranting = ? "+
                " where id = ?;";
        template.update(sql,
                user.getUid(),
                user.getEmail(),
                user.getMajor(),
                user.getGrade(),
                user.getName(),
                user.getSex(),
                user.getHead_img(),
                user.getRanting(),
                user.getLv(),
                user.isStatus(),
                user.getLast_login(),
                user.getPower(),
                user.getMax_ranting(),
                user.getId());
    }

    @Override
    public User findByUId(String uid) {
        User user=null;
        try {
            //定义sql
            String sql = "select * from user_info where uid = ?;";
            //执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), uid);
        }catch (Exception e){
        }
        if(user != null) update_date(user.getId());
        return user;
    }

    @Override
    public List<BugInfo> fineBug() {
        List<BugInfo> bugInfos = new ArrayList<>();
        try {
            //定义sql
            String sql = "select * from bug;";
            //执行sql
            bugInfos = template.query(sql, new BeanPropertyRowMapper<BugInfo>(BugInfo.class));
        }catch (Exception e){
            System.out.println(e);
        }
        return bugInfos;
    }

    @Override
    public void updateBug(BugInfo bug) {
        String sql = "insert into bug (time,text) " +
                " values(?,?);";
        template.update(sql,
                bug.getTime(),
                bug.getText()
        );
    }

    @Override
    public List<Status> findStatusById(int id) {
        List<Status> status = new ArrayList<>();
        try {
            //定义sql
            String sql = "select * from status where uid = ?;";
            //执行sql
            status = template.query(sql, new BeanPropertyRowMapper<Status>(Status.class),id);
        }catch (Exception e){
            System.out.println(e);
        }
        return status;
    }

    @Override
    public Problem findProblemById(int id) {
        Problem problem = new Problem();
        try {
            //定义sql
            String sql = "select * from problem where pid = ?;";
            //执行sql
            problem = template.queryForObject(sql, new BeanPropertyRowMapper<Problem>(Problem.class),id);
        }catch (Exception e){
            System.out.println(e);
        }
        return problem;
    }

    @Override
    public List<Status> findStatusByStaAndId(String sta,int id) {
        List<Status> status = new ArrayList<>();
        try {
            //定义sql
            String sql = "select * from status where uid = ? and status = ?;";
            //执行sql
            status = template.query(sql, new BeanPropertyRowMapper<Status>(Status.class),id,sta);
        }catch (Exception e){
            System.out.println(e);
        }
        return status;
    }

    @Override
    public List<Status> findNotStatusByStaAndId(String sta, int id) {
        List<Status> status = new ArrayList<>();
        try {
            //定义sql
            String sql = "select * from status where uid = ? and status != ? and status != '等待测试' and status != '编译错误' and status != '系统错误';";
            //执行sql
            status = template.query(sql, new BeanPropertyRowMapper<Status>(Status.class),id,sta);
        }catch (Exception e){
            System.out.println(e);
        }
        return status;
    }

    @Override
    public List<Status> findStatusByPid(String sta, int pid) {
        List<Status> status = new ArrayList<>();
        try {
            //定义sql
            String sql = "select * from status where problem_id = ? and status = ? ;";
            //执行sql
            status = template.query(sql, new BeanPropertyRowMapper<Status>(Status.class),pid,sta);
        }catch (Exception e){
            System.out.println(e);
        }
        return status;
    }

    @Override
    public List<Status> findNotStatusByPid(String sta, int pid) {
        List<Status> status = new ArrayList<>();
        try {
            //定义sql
            String sql = "select * from status where problem_id = ? and status != ? and status != '等待测试' and status != '编译错误' and status != '系统错误';";
            //执行sql
            status = template.query(sql, new BeanPropertyRowMapper<Status>(Status.class),pid,sta);
        }catch (Exception e){
            System.out.println(e);
        }
        return status;
    }

    @Override
    public int findStatusByPidCount(String sta, int pid) {
        int status = 0;
        try {
            //定义sql
            String sql = "select count(*) from status where problem_id = ? and status = ? ;";
            //执行sql
            status = template.queryForObject(sql,Integer.class,pid,sta);
        }catch (Exception e){
            System.out.println(e);
        }
        return status;
    }

    @Override
    public int findNotStatusByPidCount(String sta, int pid) {
        int status = 0;
        try {
            //定义sql
            String sql = "select count(*) from status where problem_id = ? and status != ? and status != '等待测试' and status != '编译错误' and status != '系统错误';";
            //执行sql
            status = template.queryForObject(sql,Integer.class,pid,sta);
        }catch (Exception e){
            System.out.println(e);
        }
        return status;
    }

    @Override
    public int findAllStatusCountByPid(int pid) {
        int status = 0;
        try {
            //定义sql
            String sql = "select count(*) from status where problem_id = ?;";
            //执行sql
            status = template.queryForObject(sql,Integer.class,pid);
        }catch (Exception e){
            System.out.println(e);
        }
        return status;
    }


    @Override
    public int findContestByMaster(int id) {
        int sum = 0;
        try {
            //定义sql
            String sql = "SELECT COUNT(*) FROM contest WHERE master = ?;";
            //执行sql
            sum = template.queryForObject(sql, int.class, id);
        }catch (Exception e){
        }
        return sum;
    }

    @Override
    public List<Integer> findContestByUserId(int id) {
        List<Integer> contest = new ArrayList<>();
        try {
            //定义sql
            String sql = "select contest_id from contest_sign_up where uid = ?;";
            //执行sql
            contest = template.queryForList(sql,Integer.class,id);
        }catch (Exception e){
            System.out.println(e);
        }
        return contest;
    }

    @Override
    public List<Integer> findRantingByUserId(int id) {
        List<Integer> contest = new ArrayList<>();
        try {
            //定义sql
            String sql = "select ranting from contest_rank where uid = ?;";
            //执行sql
            contest = template.queryForList(sql,Integer.class,id);
        }catch (Exception e){
            System.out.println(e);
        }
        return contest;
    }

    @Override
    public List<Contest> findNotEndContest() {
        List<Contest> contests = new ArrayList<>();
        try{
            String sql = "SELECT * FROM contest WHERE UNIX_TIMESTAMP(end_time)>UNIX_TIMESTAMP(?) ORDER BY UNIX_TIMESTAMP(start_time);";
            contests = template.query(sql, new BeanPropertyRowMapper<Contest>(Contest.class), new Date());
        }catch (Exception e){
            System.out.println(e);
        }
        return contests;
    }

    @Override
    public User findUserById(int id) {
        User user=null;
        try {
            //定义sql
            String sql = "select * from user_info where id = ?;";
            //执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), id);
        }catch (Exception e){
        }
        return user;
    }

    @Override
    public List<Contest_rank> findTreeRank() {
        List<Contest_rank> ranks = new ArrayList<Contest_rank>();
        try{
            String sql = "SELECT * FROM contest_rank WHERE user_rank >=1 AND user_rank <=3 ORDER BY contest_id DESC;";
            ranks = template.query(sql,new BeanPropertyRowMapper<Contest_rank>(Contest_rank.class));
        }catch (Exception e){

        }
        return ranks;
    }

    @Override
    public Contest findContestByCid(int id) {
        Contest contests = null;
        try{
            String sql = "SELECT * FROM contest WHERE id = ?;";
            contests = template.queryForObject(sql, new BeanPropertyRowMapper<Contest>(Contest.class), id);
        }catch (Exception e){
        }
        return contests;
    }

    @Override
    public Contest_User_info findContest_User_info(int contest_id, int uid) {
        Contest_User_info contests = null;
        try{
            String sql = "SELECT * FROM contest_user_info WHERE contest_id = ? and uid = ?;";
            contests = template.queryForObject(sql, new BeanPropertyRowMapper<Contest_User_info>(Contest_User_info.class), contest_id,uid);
        }catch (Exception e){
        }
        return contests;
    }

    @Override
    public List<Problem> findAllProblem() {
        List<Problem> problems = new ArrayList<Problem>();
        try{
            String sql = "SELECT * FROM problem WHERE is_show = 1 ORDER BY pid DESC;";
            problems = template.query(sql,new BeanPropertyRowMapper<Problem>(Problem.class));
        }catch (Exception e){

        }
        return problems;
    }

    @Override
    public List<Tag> findAllTag() {
        List<Tag> tags = new ArrayList<Tag>();
        try{
            String sql = "SELECT * FROM tag;";
            tags = template.query(sql,new BeanPropertyRowMapper<Tag>(Tag.class));
        }catch (Exception e){

        }
        return tags;
    }

    @Override
    public List<Integer> findTagByPid(int pid) {
        List<Integer> tags = new ArrayList<>();
        try {
            //定义sql
            String sql = "SELECT tag_id FROM problem_tag WHERE pid = ?;";
            //执行sql
            tags = template.queryForList(sql,Integer.class,pid);
        }catch (Exception e){
        }
        return tags;
    }

    @Override
    public List<Integer> findProblemByTag(int id) {
        List<Integer> pids = new ArrayList<>();
        try {
            //定义sql
            String sql = "SELECT pid FROM problem_tag WHERE tag_id = ?;";
            //执行sql
            pids = template.queryForList(sql,Integer.class,id);
        }catch (Exception e){
        }
        return pids;
    }

    @Override
    public List<Problem> fineProblemByStLen(int start, int len) {
        String sql="SELECT * FROM problem LIMIT ?,?;";
        return template.query(sql,new BeanPropertyRowMapper<Problem>(Problem.class),start,len);
    }

    @Override
    public int findProblemCount() {
        String sql = "select count(*) from problem where is_show = 1;";
        return template.queryForObject(sql,Integer.class);
    }

    @Override
    public Tag findTagByTId(int tid) {
        Tag tag = null;
        try{
            String sql = "SELECT * FROM tag where id = ?;";
            tag = template.queryForObject(sql,new BeanPropertyRowMapper<Tag>(Tag.class),tid);
        }catch (Exception e){

        }
        return tag;
    }

    @Override
    public int findProblemCountByTid(int tid) {
        String sql = "select count(*) from problem_tag where tag_id = ?;";
        return template.queryForObject(sql,Integer.class,tid);
    }

    @Override
    public List<User> findAllUser() {
        String sql="SELECT * FROM user_info;";
        return template.query(sql,new BeanPropertyRowMapper<User>(User.class));
    }

    @Override
    public void setContest_rank(Contest_rank rank) {
        String sql = "select count(*) from contest_rank where contest_id = ? and uid = ?;";
        Integer flag = template.queryForObject(sql, int.class, rank.getContest_id(), rank.getUid());

        if(flag == 0){
            sql = "insert into  contest_rank values(?,?,?,?);";
            template.update(sql,rank.getContest_id(),rank.getUid(),rank.getUser_rank(),rank.getRanting());
        }else{
            sql = "update contest_rank set user_rank = ?,ranting = ? where contest_id = ? and uid = ?;";
            template.update(sql,rank.getUser_rank(),rank.getRanting(),rank.getContest_id(),rank.getUid());
        }

    }
}
