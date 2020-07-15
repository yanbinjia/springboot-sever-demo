package com.demo.study.testclass.jvm;

public class GcTest {
    public static void main(String[] args) throws InterruptedException {

        // 添加的参数：-XX:+PrintGCDetails
        // -Xloggc:logs/gc.log
        byte[] allocation1, allocation2, allocation3, allocation4, allocation5;
        allocation1 = new byte[28000 * 1000];
        allocation2 = new byte[1000 * 1024];
        allocation3 = new byte[1000 * 1024];
        allocation4 = new byte[1000 * 1024];
        allocation5 = new byte[1000 * 1024];
        long i = 0;
        for (; ; ) {
            Thread.sleep(1000);
            byte[] bytes = new byte[28000 * 1000];
            byte[] bytes2 = new byte[18000 * 1000];
            bytes = null;
            bytes2 = null;
            System.out.println("i" + i++);
        }

    }
}
