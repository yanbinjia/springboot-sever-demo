/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-07T13:11:59.809+08:00
 */

package com.demo.server.common.utils.thread.runner;

import com.demo.server.common.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MultiTreadRunner {
    private Class<?> taskMethodClazz = null;
    int threadNum = 1;
    int loopInThreadSize = 1;

    public MultiTreadRunner(Class<?> taskMethodClazz, int threadNum,
                            int loopInThreadSize) throws Exception {
        System.out.println("taskMethodClazz class: " + taskMethodClazz.getTypeName());

        if (!TaskMethod.class.isAssignableFrom(taskMethodClazz)) {
            throw new Exception("Param error: taskMethodClazz must implements " + TaskMethod.class.getTypeName());
        }
        if (threadNum <= 0 || loopInThreadSize <= 0) {
            throw new Exception("Param error: threadNum and loopInThreadSize must > 0");
        }

        this.taskMethodClazz = taskMethodClazz;
        this.threadNum = threadNum;
        this.loopInThreadSize = loopInThreadSize;
    }

    public void run() {
        int successCount = 0;
        int failCount = 0;
        List<FutureTask<TaskResult>> futureList = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(threadNum);

        System.out.println(">>> Task start at " + DateUtil.getCurrentDateTimeStr());

        for (int i = 0; i < threadNum; i++) {
            try {
                TaskMethod taskMethod = (TaskMethod) taskMethodClazz.newInstance();
                TaskCallable taskCallable = new TaskCallable(loopInThreadSize, taskMethod);
                FutureTask<TaskResult> future = new FutureTask<>(taskCallable);
                executor.submit(future);
                futureList.add(future);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        TaskResult result = null;
        for (FutureTask<TaskResult> futureTask : futureList) {
            try {
                result = futureTask.get();
                successCount += result.getSuccessCount();
                failCount += result.getFailCount();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long totalCost = endTime - startTime;
        double successRate = successCount <= 0 ? 0 : Double.valueOf(successCount) / (failCount + successCount);
        double throughput = Double.valueOf(failCount + successCount) / (totalCost / 1000d);

        System.out.println(">>> summary info: ");

        System.out.println("cost: " + totalCost + "ms");
        System.out.println("throughput: " + String.format("%.2f", throughput) + "/sec");
        System.out.println("successCount: " + successCount);
        System.out.println("failCount: " + failCount);
        System.out.println("successRate: " + String.format("%.2f", successRate * 100) + "%");

        System.out.println(">>> Task end at " + DateUtil.getCurrentDateTimeStr());

        executor.shutdown();
    }
}
