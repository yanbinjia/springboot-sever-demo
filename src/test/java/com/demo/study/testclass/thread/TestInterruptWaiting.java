package com.demo.study.testclass.thread;

import java.util.Date;

public class TestInterruptWaiting {
    /**
     * interrupt 会让阻塞waitting状态的线程，抛出异常并退出
     * <p>
     * 本例:java.lang.InterruptedException: sleep interrupted
     */
    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 5) {
                    System.out.println("i=" + i++);
                }
                try {
                    Thread.sleep(1000 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("" + Thread.currentThread().getName() + " end. " + new Date());

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
