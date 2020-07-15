package com.demo.study.testclass.thread;


import java.util.concurrent.atomic.AtomicInteger;

public class TaskResult {
    int successCount = 0;
    int failCount = 0;

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
}