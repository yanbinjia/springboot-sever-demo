package com.demo.study.testclass.thread;

import lombok.SneakyThrows;

import java.util.Date;

public class TestDaemon {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    Thread.sleep(1000 * 1);
                    System.out.println("" + Thread.currentThread().getName() + " end. " + new Date());
                }

            }
        });

        System.out.println("thread.setDaemon(true)");
        thread.setDaemon(true);
        System.out.println("thread.start()");
        thread.start();

        Thread.sleep(1000 * 2);

    }
}
