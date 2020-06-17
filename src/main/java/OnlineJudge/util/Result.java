package OnlineJudge.util;

public class Result {
    public static String intToString(int sta){
        if(sta == 0) return "等待测试";
        if(sta == 1) return "等待重判";
        if(sta == 2) return "正在运行";
        if(sta == 3) return "正在运行";
        if(sta == 4) return "答案正确";
        if(sta == 5) return "格式错误";
        if(sta == 6) return "答案错误";
        if(sta == 7) return "时间超限";
        if(sta == 8) return "内存超限";
        if(sta == 9) return "OL";
        if(sta ==10) return "运行错误";
        if(sta ==11) return "编译错误";
        if(sta ==12) return "CO";
        return "未知错误";
    }
}
