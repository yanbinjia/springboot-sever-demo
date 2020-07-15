package com.demo.study.testclass.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;


public class TaskCallable implements Callable<TaskResult> {
    TaskMethod taskMethod = null;
    int loopInThreadSize = 1;
    AtomicInteger successCount = new AtomicInteger(0);
    AtomicInteger failCount = new AtomicInteger(0);

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
                successCount.incrementAndGet();
            } else {
                failCount.incrementAndGet();
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
