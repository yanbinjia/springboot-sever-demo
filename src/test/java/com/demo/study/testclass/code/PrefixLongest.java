package com.demo.study.testclass.code;

import java.util.Arrays;

public class PrefixLongest {
    public static String replaceSpace(String[] strs) {
        // 数组长度
        int len = strs.length;
        // 用于保存结果
        StringBuilder res = new StringBuilder();
        // 给字符串数组的元素按照升序排序(包含数字的话，数字会排在前面)
        Arrays.sort(strs);
        int m = strs[0].length();
        int n = strs[len - 1].length();
        int num = Math.min(m, n);
        for (int i = 0; i < num; i++) {
            if (strs[0].charAt(i) == strs[len - 1].charAt(i)) {
                res.append(strs[0].charAt(i));
            } else
                break;

        }
        return res.toString();
    }

    // 测试
    public static void main(String[] args) {
        String[] strs = {"cas", "castomer", "caz", "car", "cat"};
        Arrays.sort(strs);
        System.out.println(Arrays.toString(strs));
        System.out.println(PrefixLongest.replaceSpace(strs));// ca
    }
}
