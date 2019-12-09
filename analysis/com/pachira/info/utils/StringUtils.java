package com.pachira.info.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 王添宝
 * @date 2019-11-13 15:33
 */
public class StringUtils {
    public static char[] numSource = new char[] {
            '零','幺','一','二','三','四','五','六','七','八','九','，','。','！','？'
    };
    public static char[] numTarget = new char[] {
            '0','1','1','2','3','4','5','6','7','8','9',' ',' ',' ',' '
    };

    public static char[] biaodians = new char[] {'，','。','！'};


    public static String transStr(String text) {
        char[] chs = text.toCharArray();
        int j = 0;
        for (int i = 0; i < chs.length; i++) {
            int findIndex = indexOf(numSource, chs[i]);
            if (findIndex == -1) {
                chs[j] = chs[i];
                j++;
            } else {
                if (numTarget[findIndex] != ' ') {
                    chs[j] = numTarget[findIndex];
                    j++;
                }
            }
        }
        return new String(chs, 0, j);
    }
    public static int indexOf(char[] chs, char target) {
        for (int i = 0; i < chs.length; i++) {
            if (target == chs[i]) {
                return i;
            }
        }
        return -1;
    }

    public static List<Integer> indexOf(String source, String target) {
        List<Integer> res = new ArrayList<>();
        int index = 0;
        int newStart = 0;
        while((index = source.indexOf(target)) != -1) {
            res.add(newStart + index);
            newStart = index + target.length();
            if (newStart == source.length()) {
                break;
            }
            source = source.substring(newStart);
        }
        return res;
    }

    public static int getRealIndex(int index, String text) {
        int temp = 0;
        A: for (int i = 0; i < text.length(); i++) {
            for (int j = 0; j < biaodians.length; j++) {
                if (biaodians[j] == text.charAt(i)) {
                    continue A;
                }
            }
            if (index == temp) {
                return i;
            }
            temp++;
        }
        return index;
    }

}
