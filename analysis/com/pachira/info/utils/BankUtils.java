package com.pachira.info.utils;

/**
 * @author: 王添宝
 * @date 2019-11-14 18:33
 */
public class BankUtils {
    public static boolean isYinlian(String cardId) {
        char[] chs = cardId.toCharArray();
        int[] temp = new int[chs.length];
        int sum = 0;
        for (int i = chs.length - 2; i >= 0; i-=2) {
            temp[i] = (chs[i] - '0') * 2;
            temp[i] = temp[i]/10 + temp[i]%10;
            sum += temp[i];
        }
        for (int i = chs.length - 3; i >= 0; i-=2) {
            temp[i] = (chs[i] - '0');
            sum += temp[i];
        }
        int lastVal = 10 - sum % 10;
        return chs[chs.length - 1] == lastVal + '0';
    }
}
