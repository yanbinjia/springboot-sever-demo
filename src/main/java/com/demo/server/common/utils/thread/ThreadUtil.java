/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-07T13:19:36.401+08:00
 */

package com.demo.server.common.utils.thread;

import org.apache.commons.lang3.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThreadUtil {
    private static final Logger logger = LoggerFactory.getLogger(ThreadUtil.class);

    /**
     * Each tasks blocks 90% of the time, and works only 10% of its
     * lifetime. That is, I/O intensive pool
     *
     * @return io intesive Thread pool size
     */
    public static int ioIntesivePoolSize() {
        double blockingCoefficient = 0.9;
        return poolSize(blockingCoefficient);
    }

    public static int cpuIntesivePoolSize() {
        double blockingCoefficient = 0.1;
        return poolSize(blockingCoefficient) + 1;
    }

    /**
     * Number of threads = Number of Available Cores / (1 - Blocking
     * Coefficient) where the blocking coefficient is between 0 and 1.
     * <p>
     * A computation-intensive task has a blocking coefficient of 0, whereas an
     * IO-intensive task has a value close to 1,
     * so we don't have to worry about the value reaching 1.
     *
     * @param blockingCoefficient the coefficient
     * @return Thread pool size
     */
    public static int poolSize(double blockingCoefficient) {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        int poolSize = (int) (numberOfCores / (1 - blockingCoefficient));
        return poolSize;
    }

    public static int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.error("sleep error", e);
        }
    }

    /**
     * 停止线程池
     * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * 如果仍人超時，則強制退出.
     * 另对在shutdown时线程本身被调用中断做了处理.
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                        logger.info("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 打印线程异常信息
     */
    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            logger.error(t.getMessage(), t);
        }
    }

    public static void main(String[] args) {
        System.out.println("availableProcessors=" + ThreadUtil.availableProcessors());
        System.out.println("ioIntesivePoolSize=" + ThreadUtil.ioIntesivePoolSize());

        ThreadUtil.sleep(1000);

        System.out.println("cpuIntesivePoolSize=" + ThreadUtil.cpuIntesivePoolSize());
        System.out.println("poolSize=" + ThreadUtil.poolSize(0.5));


        ThreadUtils.getAllThreads().stream().forEach(thread -> {
            System.out.println(thread.getId() + ":" + thread.getName());
        });

    }
}
