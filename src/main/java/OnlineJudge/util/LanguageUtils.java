package OnlineJudge.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;

public class LanguageUtils {
    public static String lidtoString(int id){
        String language[] = { "C", "C++", "pascal", "Java", "Ruby", "Bash", "Python", "php", "perl", "c#", "Objective c", "free basic", "scheme guile" };
                             //0     1        2      3         4       5       6         7     8       9       10                11            12
        return language[id];
    }
    public static int stringtoid(String language){
        if("C".equalsIgnoreCase(language)) return 0;
        if("C++".equalsIgnoreCase(language)) return 1;
        if("CC".equalsIgnoreCase(language)) return 1;
        if("pascal".equalsIgnoreCase(language)) return 2;
        if("java".equalsIgnoreCase(language)) return 3;
        if("Ruby".equalsIgnoreCase(language)) return 4;
        if("Bash".equalsIgnoreCase(language)) return 5;
        if("python".equalsIgnoreCase(language)) return 6;
        if("php".equalsIgnoreCase(language)) return 7;
        if("perl".equalsIgnoreCase(language)) return 8;
        if("c#".equalsIgnoreCase(language)) return 9;
        if("Objective c".equalsIgnoreCase(language)) return 10;
        if("free basic".equalsIgnoreCase(language)) return 11;
        if("scheme guile".equalsIgnoreCase(language)) return 12;
        return -1;
    }
}
