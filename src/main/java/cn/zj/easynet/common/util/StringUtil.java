/**
 * 
 */
package cn.zj.easynet.common.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liyalong
 */
public class StringUtil {
    public static String Bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static byte[] HexString2Bytes(String src) {
        int len = src.length() / 2;
        byte[] ret = new byte[len];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < len; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
            .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
            .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static void ascStrings(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return;
        }
        Collections.sort(strings, new Comparator<String>() {

            @Override
            public int compare(String s1, String s2) {
                int len1 = s1.length();
                int len2 = s2.length();

                int len = len1 < len2 ? len1 : len2;
                for (int i = 0; i < len; i++) {
                    char c1 = s1.charAt(i);
                    char c2 = s2.charAt(i);
                    if (c1 != c2) {
                        return c1 - c2;
                    }
                }
                return len1 - len2;
            }
        });
    }

    /**
     * 判断str is empty,true is empty
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.equals(""));
    }

    public static String formatBytes(long bytes) {
        if (bytes < 1024 * 1024)
            return String.format("%.2fKB", (double) bytes / 1024);
        else if (bytes < 1024 * 1024 * 1024)
            return String.format("%.2fMB", (double) bytes / 1024 / 1024);
        else
            return String.format("%.2fGB", (double) bytes / 1024 / 1024 / 1024);
    }
    
    /**
     * key1=value1&key2=value2参数解释，返回对应的Map结构
     * 
     * @param msg
     * @return
     */
    public static Map<String, String> parseParameter(String msg) {
        if (msg == null || msg.equals("")) {
            return null;
        }

        Map<String, String> result = new HashMap<String, String>();
        String[] params = msg.split("&");
        for (String param: params) {
            fillParams(result, param);
        }

        return result;
    }

    /**
     * key=value参数填充到map中
     * 
     * @param map
     * @param param
     */
    private static void fillParams(Map<String, String> map, String param) {
        int len = param.indexOf("=");
        if (len < 0) {
            return;
        }

        String key = param.substring(0, len);
        String value = param.substring(len + 1);
        
        map.put(key, value);
    }
}
