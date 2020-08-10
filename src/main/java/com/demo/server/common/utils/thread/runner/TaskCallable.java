/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-07T13:11:59.815+08:00
 */

package com.demo.server.common.utils.thread.runner;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.LongAdder;

public class TaskCallable implements Callable<TaskResult> {
    private TaskMethod taskMethod = null;
    private int loopInThreadSize = 1;
    private LongAdder successCount = new LongAdder();
    private LongAdder failCount = new LongAdder();

    public TaskCallable(int loopInThreadSize, TaskMethod taskMethod) {
        this.loopInThreadSize = loopInThreadSize;
        this.taskMethod = taskMethod;
    }

    @Override
    public TaskResult call() throws Exception {

        TaskResult taskResult = new TaskResult();
        long start = System.currentTimeMillis();
        int currentLoop = 0;

        System.out.printf("%s run loop start. %n", Thread.currentThread().getName(), currentLoop);

        while (currentLoop < loopInThreadSize) {
            currentLoop++;
            if (taskMethod.run()) {
                successCount.increment();
            } else {
                failCount.increment();
            }
        }
        long end = System.currentTimeMillis();

        taskResult.setSuccessCount(this.successCount.intValue());
        taskResult.setFailCount(this.failCount.intValue());

        System.out.printf("%s run [%d] loop done. cost [%d] ms %n", Thread.currentThread().getName(),
                currentLoop, (end - start));

        return taskResult;
    }

}
