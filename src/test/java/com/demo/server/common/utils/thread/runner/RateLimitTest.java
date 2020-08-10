/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-09T16:41:33.516+08:00
 */

package com.demo.server.common.utils.thread.runner;

import com.demo.server.common.utils.thread.ThreadUtil;
import org.junit.Test;

public class RateLimitTest {

    @Test
    public void rateLimitTest() throws Exception {
        MultiTreadRunner runner = new MultiTreadRunner(RateLimitTaskMethod.class, ThreadUtil.availableProcessors(), 10);
        runner.run();
    }
}
