package com.demo.server.common.utils.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * ThreadUtil Tester.
 *
 * @author yanbinjia@126.com
 * @version 1.0
 * @since <pre>8月 9, 2020</pre>
 */
@Slf4j
public class ThreadUtilTest {

    @Test
    public void testIoIntesivePoolSize() throws Exception {
        log.debug("ioIntesivePoolSize=" + ThreadUtil.ioIntesivePoolSize());
        Assert.assertTrue(ThreadUtil.ioIntesivePoolSize() > 0);
    }

    @Test
    public void testCpuIntesivePoolSize() throws Exception {
        log.debug("cpuIntesivePoolSize=" + ThreadUtil.cpuIntesivePoolSize());
        Assert.assertTrue(ThreadUtil.cpuIntesivePoolSize() > 0);
    }

    @Test
    public void testPoolSize() throws Exception {
        log.debug("poolSize=" + ThreadUtil.poolSize(0.5));
        Assert.assertTrue(ThreadUtil.poolSize(0.5) > 0);
    }

    @Test
    public void testAvailableProcessors() throws Exception {
        log.debug("availableProcessors=" + ThreadUtil.availableProcessors());
        Assert.assertTrue(ThreadUtil.availableProcessors() > 0);

    }

    @Test
    public void testSleep() throws Exception {
        ThreadUtil.sleep(20);
    }

    @Test
    public void testGetThread() throws Exception {
        Assert.assertNotNull(ThreadUtil.getAllThread());

        Collection<Thread> list = ThreadUtil.getAllThread();
        list.stream().forEach(thread -> {
            log.debug(thread.getId() + ":" + thread.getName());
        });

        Assert.assertTrue(ThreadUtil.getMainThread().getId() == 1);
    }
}
