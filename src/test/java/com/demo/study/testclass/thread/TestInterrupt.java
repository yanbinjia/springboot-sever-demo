package com.demo.study.testclass.thread;

import java.util.Date;

public class TestInterrupt {
    /**
     * 使用interrupt优雅退出的例子，interrupt 只是个中断标志，并不是interrupt让正常的线程退出，而且状态判断逻辑
     */

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("" + Thread.currentThread().getName() + " say hello.");
                }
            }
        });

        thread.start();

        Thread.sleep(1000);

        System.out.println("thread.interrupt(); " + new Date());
        thread.interrupt();

        // 等待子线程执行完毕
        thread.join();

    }

}
