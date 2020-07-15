package com.demo.study.testclass;

public class JustTest {
    public static void main(String[] args) throws InterruptedException {
        String str = "0xxx0";
        System.out.println(str.length());
        String chars = str.substring(1,str.length());
        System.out.println(chars);
    }
}
