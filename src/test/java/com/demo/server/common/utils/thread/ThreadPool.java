package com.demo.server.common.utils.thread;

import java.util.concurrent.*;

public class ThreadPool {
    public static void main(String[] args) {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(12);

        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(12, 20,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(20));


    }
}
