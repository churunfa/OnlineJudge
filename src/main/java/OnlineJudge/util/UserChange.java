package OnlineJudge.util;

import OnlineJudge.domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 0 0,99         #C9C9C9
 * 1 100,299      #7CFC00
 * 2 300,499      #7CCD7C
 * 3 500,699      #9B30FF
 * 4 700,999      #FFA500
 * 5 1000,1299    #FF0000
 * 6 1300         #000
 */

public class UserChange {
    public static User changeNameLv(User user){
        String re = ">(.*?)<";
        String name ="";
        Matcher matcher = Pattern.compile(re).matcher(user.getName());
        while(matcher.find()) name= matcher.group();
        name = name.substring(1,name.length()-1);

        if(user.getRanting() <  100){
            user.setLv(0);
            user.setName("<span style='color: #C9C9C9'>"+name+"</span>");
        }else if(user.getRanting() < 300){
            user.setLv(1);
            user.setName("<span style='color: #7CFC00'>"+name+"</span>");
        }else if(user.getRanting() < 500){
            user.setLv(2);
            user.setName("<span style='color: #7CCD7C'>"+name+"</span>");
        }else if(user.getRanting() < 700){
            user.setLv(3);
            user.setName("<span style='color: #9B30FF'>"+name+"</span>");
        }else if(user.getRanting()  < 1000){
            user.setLv(4);
            user.setName("<span style='color: #FFA500'>"+name+"</span>");
        }else if(user.getRanting() < 1300){
            user.setLv(5);
            user.setName("<span style='color: #FF0000'>"+name+"</span>");
        }else {
            user.setLv(6);
            user.setName("<span style='color: #000'>"+name+"</span>");
        }

        return user;
    }
}
