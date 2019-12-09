package com.pachira.info.utils;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 王添宝
 * @date 2019-11-13 17:00
 */
public class CommonUtils {
    public static int toInt(Object obj) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (Throwable e) {
            return -1;
        }
    }

    public static void close(Closeable... clses) {
        for (Closeable cls : clses) {
            try {
            	if(cls == null) {
            		continue;
            	}
                cls.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void close(AutoCloseable... clses) {
        for (AutoCloseable cls : clses) {
            try {
            	if(cls == null) {
            		continue;
            	}
                cls.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isEmptyCollection(Collection<?> it) {
        return it == null || it.isEmpty();
    }

    public static String transNum(String str) {
        int dian = str.indexOf("点");
        String xiao = "";
        if (dian != -1) {
            xiao = "." + str.substring(dian + 1);
            str = str.substring(0, dian);
        }
        Map<Character, Integer> jinzhiMap = new HashMap<>();
        jinzhiMap.put('十', 10);
        jinzhiMap.put('百', 100);
        jinzhiMap.put('千', 1000);
        jinzhiMap.put('万', 10000);
        jinzhiMap.put('亿', 100000000);
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(c == '两') {
                c = '2';
            }
            if (c >= '0' && c <= '9') {
                result += (c - '0');
            } else if(i == 0) {
                Integer jinzhi = jinzhiMap.get(c);
                if (jinzhi == null) {
                    return null;
                }
                result = jinzhi;
            } else {
                Integer jinzhi = jinzhiMap.get(c);
                if (jinzhi == null) {
                    return null;
                }
                result = result / jinzhi * jinzhi + result % jinzhi * jinzhi;
            }
        }
        return result + xiao;
    }

    public static void println(OutputStream out, Object obj) {
        if (out instanceof PrintStream) {
            ((PrintStream) out).println(obj);
            return;
        }
        BufferedWriter bw = null;
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (obj != null) {
                bw.write(obj.toString());
            }
            bw.newLine();
            bw.flush();
            osw.flush();
            out.flush();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static boolean containsNum(String source) {
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

}
