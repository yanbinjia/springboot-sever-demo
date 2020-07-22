package com.demo.study.testclass.thread;

import lombok.SneakyThrows;

import java.util.Date;

public class TestInterruptWaiting2 {
    static Object object = new Object();

    /**
     * interrupt 会让阻塞waitting状态的线程，抛出异常并退出
     * <p>
     * 本例:Exception in thread "Thread-0" java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                int i = 0;
                while (i < 5) {
                    System.out.println("i=" + i++);
                }
                // wait 必须伴随synchronized使用
                synchronized (object) {
                    object.wait();
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
