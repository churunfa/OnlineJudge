package OnlineJudge.service.impl;

import OnlineJudge.dao.impl.BlogDaoImpl;
import OnlineJudge.dao.impl.ProblemDaoImpl;
import OnlineJudge.dao.impl.UserDaoImpl;
import OnlineJudge.domain.Problem;
import OnlineJudge.domain.Solution;
import OnlineJudge.domain.SolutionInfo;
import OnlineJudge.domain.User;
import OnlineJudge.service.BlogService;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlogServiceImpl implements BlogService {
    BlogDaoImpl blogDao = new BlogDaoImpl();
    ProblemDaoImpl problemDao = new ProblemDaoImpl();
    UserDaoImpl userDao = new UserDaoImpl();
    @Override
    public List<SolutionInfo> findSolutionInfoBy(int pid,int uid) {
        List<Solution> blogs = blogDao.findBlogByPid(pid);

        List<SolutionInfo> info = new ArrayList<SolutionInfo>();

        for(Solution blog:blogs){

            if(!blog.isShow() && blog.getMaster() != uid) continue;

            SolutionInfo solutionInfo = new SolutionInfo();
            User user = userDao.findUserById(blog.getMaster());

            String re = ">(.*?)<";

            String name ="";
            Matcher matcher = Pattern.compile(re).matcher(user.getName());
            while(matcher.find()) name= matcher.group();;

            name = name.substring(1,name.length()-1);

            user.setName(name);

            solutionInfo.setUser(user);
            solutionInfo.setSolution(blog);
            info.add(solutionInfo);
        }

        return info;
    }

}
