package com.demo.study.testclass.code;

import java.util.Arrays;

public class StrPattern {
    private static int kmp(String str, String pattern) {
        int i = 0;
        int j = 0;
        int[] next = getNext(pattern); // 分析字串
        System.out.println(Arrays.toString(next));
        while (i < str.length() && j < pattern.length()) {
            //ABCER
            // CD
            if (j == -1 || str.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j]; //-1 j表示当前字符比较失败了
            }
        }

        if (j == pattern.length()) {
            return i - j;
        } else {
            return -1;
        }
    }

    private static int[] getNext(String pattern) {
        int[] next = new int[pattern.length()];
        int k = -1;
        int j = 0;
        next[0] = -1;
        // 检测每一个字符之前的字符串，计算它们前后缀的最大长度，然后
        // 把长度记录在当前的next数组位置当中
        while (j < pattern.length() - 1) {
            if (k == -1 || pattern.charAt(k) == pattern.charAt(j)) {
                ++j;
                ++k;
                // if主要处理ABCABC这种情况的优化
                if (pattern.charAt(k) == pattern.charAt(j)) {
                    next[j] = next[k];
                } else {
                    next[j] = k;
                }
            } else {
                k = next[k]; // 前后缀长度需要缩减
            }
        }

        return next;
    }

    private static int find(String str, String pattern) {
        int i = 0;
        int j = 0;
        while (i < str.length() && j < pattern.length()) { // O（n^2）
            if (str.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else {
                i = i - j + 1;
                j = 0;
            }
        }

        if (j == pattern.length()) {
            return i - j;
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        String s = "000001000001";
        String t = "00001";
        String t1 = "AAAAAAA";
        int pos = kmp(s, t);
        System.out.println("pos:" + pos);
    }
}
