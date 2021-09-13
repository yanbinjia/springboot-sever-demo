package com.demo.study.testclass.code;

public class JInZhiZhuanHuan {
    public String solve(int M, int N) {
        // write code here
        if (M == 0) return "0";
        String s = "0123456789ABCDEF";
        StringBuffer sb = new StringBuffer();
        boolean f = false;
        if (M < 0) {
            f = true;
            M = -M;
        }
        while (M != 0) {
            sb.append(s.charAt(M % N));
            M /= N;
        }
        if (f) sb.append("-");
        return sb.reverse().toString();
    }
}

