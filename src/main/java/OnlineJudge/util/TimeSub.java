package OnlineJudge.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSub {
    public static String Sub(Date date2, Date date1){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long l = date2.getTime() - date1.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String ans;
        if(day != 0) ans = day + "天" + hour + "小时" + min +"分" ;
        else ans = hour + "小时" + min +"分" ;
        return ans;
    }
}
