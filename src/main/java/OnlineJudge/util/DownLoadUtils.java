package OnlineJudge.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import static java.lang.Integer.toHexString;


public class DownLoadUtils {

    public static String getFileName(String agent, String filename) throws UnsupportedEncodingException {
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            Base64.Encoder encoder = Base64.getEncoder();
            filename = "=?utf-8?B?" +encoder.encodeToString(filename.getBytes("utf-8")) + "?=";
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
    public static String encodeUrlToGB2312(String url)
            throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            String s = url.substring(i, i + 1);
            byte[] bytes = s.getBytes("gb2312");
            if (bytes.length == 1) { // 如果为一个字节则直接加入StringBuffer（中文至少为两个字节，一个字节不可能为中文）
                if (bytes[0] == ' ')
                    sb.append("%20");
                else
                    sb.append(s);
            } else {
                for (int j = 0; j < bytes.length; j++) {
                    sb.append("%" + toHexString(bytes[j]));
                }
            }
        }
        return sb.toString();
    }
}
