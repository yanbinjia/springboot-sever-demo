package com.demo.study.testclass.code;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Atoi {
    public int atoi(String str) {
        if (str == null || "".equals(str.trim()))
            return 0;

        str = str.trim(); // must!

        int len = str.length();
        boolean positive = true;
        long sum = 0;
        int rs = 0;

        for (int i = 0; i < len; i++) {
            char var = str.charAt(i);
            if (i == 0) {
                if (var == '-') {
                    positive = false;
                    continue;
                }
                if (var == '+') {
                    continue;
                }
            }

            if (var >= '0' && var <= '9') {
                sum = sum * 10 + (var - '0');
            } else {
                break;
            }
        }

        if (positive) {
            rs = sum < Integer.MAX_VALUE ? (int) sum : Integer.MAX_VALUE;
        } else {
            rs = -sum < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) (-sum);
        }

        return rs;

    }


    public int myAtoi(String str) {
        if (str == null || str.trim().length() == 0) {
            return 0;
        }
        long resLong = 0;
        int resInt = 0;
        str = str.trim(); // must!
        char[] chars = str.toCharArray();

        int length = chars.length;
        int flag = 1;
        char tmpChar;

        boolean realStart = false;
        int realNumLen = 0;
        int maxLenOfInt = String.valueOf(Integer.MAX_VALUE).length() + 1;

        for (int i = 0; i < length; i++) {
            tmpChar = chars[i];
            if (i == 0) {
                if (tmpChar == '-') {
                    flag = -1;
                    continue;
                } else if (tmpChar == '+') {
                    flag = 1;
                    continue;
                }
            }

            // 处理前面是0的情况:00000000000000000000123
            if (!realStart && tmpChar > '0') {
                realStart = true;
            }

            if (tmpChar >= '0' && tmpChar <= '9') {
                // 处理int越界
                if (realStart) {
                    realNumLen++;
                    if (realNumLen > maxLenOfInt) {
                        break;
                    }
                    resLong = resLong * 10 + (tmpChar - '0');
                }
            } else {
                break;
            }
        }

        if (flag == 1) {
            resInt = resLong > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) resLong;
        } else {
            resInt = -resLong < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) (-resLong);
        }

        return resInt;

    }

    public static void main(String[] args) {
        Atoi atoi = new Atoi();
        System.out.println(atoi.myAtoi("000000000000000000000000123"));
        System.out.println(atoi.myAtoi("-000000000000000000000000123"));
        System.out.println(atoi.myAtoi("99999999999999999999999999"));
        System.out.println(atoi.myAtoi("-   99999999999999999999999999"));

        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);

        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
    }
}
