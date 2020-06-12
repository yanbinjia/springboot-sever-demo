package com.demo.server.common.util;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RateLimitTest {

	AtomicInteger successCount = new AtomicInteger(0);
	AtomicInteger rateLimitCount = new AtomicInteger(0);

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testMultiThread() {
		int threadNum = 10;
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < threadNum; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Response response = Jsoup.connect("http://127.0.0.1:6673/test/rate/test")
								.ignoreContentType(true).execute();
						String jsonStr = response.body();

						JSONObject jsonObject = JSON.parseObject(jsonStr);
						int code = jsonObject.getInteger("code");
						if (code == 0) {
							successCount.addAndGet(1);
						} else {
							rateLimitCount.addAndGet(1);
						}

						System.out.println("response: " + jsonObject.toJSONString());

					} catch (IOException e) {
						e.printStackTrace();
					}

					countDownLatch.countDown();
				}
			});
			thread.start();
		}
		try {
			countDownLatch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();

		System.out.println("cost: " + (endTime - startTime) + "ms");
		System.out.println("successCount: " + successCount.intValue());
		System.out.println("rateLimitCount: " + rateLimitCount.intValue());

	}

}
