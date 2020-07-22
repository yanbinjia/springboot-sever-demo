package com.demo.server.common.util;

public final class ThreadPoolUtil {

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


    public static void main(String[] args) {
        System.out.println("availableProcessors=" + ThreadPoolUtil.availableProcessors());
        System.out.println(ThreadPoolUtil.ioIntesivePoolSize());
        System.out.println(ThreadPoolUtil.cpuIntesivePoolSize());
        System.out.println(ThreadPoolUtil.poolSize(0.5));
    }
}
