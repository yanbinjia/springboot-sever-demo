package com.demo.study.testclass.juc;

import java.util.concurrent.CountDownLatch;

public class TestMutex {
    static Mutex mutex = new Mutex();
    static int sumSafe = 0;
    static int sum = 0;

    public static void increment() {
        sum++;
    }

    public static void incrementSafe() {
        mutex.lock();
        sumSafe++;
        mutex.unlock();
    }

    public static void main(String[] args) {
        int threadNum = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        incrementSafe();
                        increment();
                    }
                    countDownLatch.countDown();
                }
            });
            thread.start();
        }

        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("sumSafe=" + sumSafe);
        System.out.println("sum=" + sum);
    }
}
