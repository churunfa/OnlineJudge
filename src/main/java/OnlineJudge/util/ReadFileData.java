package OnlineJudge.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ReadFileData{
    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null)
            {
                String regex = "\\$\\$";
                s = s.replaceAll("\\$\\$","\\$");
                regex = "<";
                s = s.replaceAll(regex,"&lt;");
                regex = ">";
                s = s.replaceAll(regex,"&gt;");
                //使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String txt2String(File file,boolean flag){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null)
            {
                if(flag){
                    String regex = "\\$\\$";
                    s = s.replaceAll("\\$\\$","\\$");
                }
                //使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
}

