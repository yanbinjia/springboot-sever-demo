package com.demo.study.testclass.juc;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderTest {
    @SneakyThrows
    public static void main(String[] args) {

        AtomicLong atomicLong = new AtomicLong(0);
        LongAdder longAdder = new LongAdder();

        int threadNum = 20;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        longAdder.increment();
                        atomicLong.incrementAndGet();
                    }
                }
            });
            thread.start();

            // 等待进程执行完成
            thread.join();
        }

        System.out.println("cost:" + (System.currentTimeMillis() - startTime));
        System.out.println(longAdder.sum());
    }
}
