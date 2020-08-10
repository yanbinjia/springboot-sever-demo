/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-07T13:11:59.823+08:00
 */

package com.demo.server.common.utils.thread.runner;

public interface TaskMethod {
    /**
     * 运行任务具体的执行方法
     *
     * @return 成功或失败, true/false
     */
    boolean run();
}
