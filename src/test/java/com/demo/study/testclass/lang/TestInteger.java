package com.demo.study.testclass.lang;

public class TestInteger {
    public static void main(String[] args) {
        // 2147483647
        System.out.println(Integer.MAX_VALUE);
        // -2147483648
        System.out.println(Integer.MIN_VALUE);
        // 1111011
        System.out.println(Integer.toBinaryString(123));

        System.out.println(Integer.valueOf("123"));
    }
}
