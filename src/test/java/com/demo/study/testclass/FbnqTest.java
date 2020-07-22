package com.demo.study.testclass;

import java.util.Arrays;

public class FbnqTest {
    public int fibonacci(int n) {

        if (n < 1) {
            return 0;
        }
        if (n < 3) {
            return 1;
        }

        return fibonacci(n - 2) + fibonacci(n - 1);

    }

    static long[] buffer = new long[93];

    static {
        buffer[1] = 1;
    }

    public long fibBuffer(int num) {
        if (num < 1 || num > 92)
            return 0;
        if (buffer[num] == 0)
            buffer[num] = fibBuffer(num - 1) + fibBuffer(num - 2);
        return buffer[num];
    }

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

    public static void main(String[] args) {
        FbnqTest fbnqTest = new FbnqTest();
        System.out.println(fbnqTest.fibBuffer(6));



        String s = FbnqTest.replaceSpace(new String[]{"dadaaddd","dadsssssssss","dadaax"});
        System.out.println(s);
        System.out.println(s.charAt(1));

        for (int i=0;i<s.length();i++){
            char charStr = s.charAt(i);
            System.out.println(charStr);
        }
    }
}
