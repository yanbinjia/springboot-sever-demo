package com.demo.study.testclass.juc;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class TestPark {
    public static void main(String[] args) throws Exception {

        Thread boyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("boy: 我要吃鸡");
                LockSupport.park();
                System.out.println("boy: park1");
                LockSupport.park(); // 第二次会阻塞住，因为只有一个permit
                System.out.println("boy: park2");
                System.out.println("boy: 开始吃鸡了");
            }
        });

        Thread girlThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    System.out.println(">>> girl: sleep 4s");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("girl: 允许");
                LockSupport.unpark(boyThread); // unpark两次，但是permit不会叠加
                LockSupport.unpark(boyThread);

                try {
                    Thread.sleep(4000);
                    System.out.println(">>> girl: sleep 4s");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LockSupport.unpark(boyThread);// 释放第二次park
            }
        });

        boyThread.start();
        girlThread.start();


        ReentrantLock reentrantLock = new ReentrantLock();
    }
}
