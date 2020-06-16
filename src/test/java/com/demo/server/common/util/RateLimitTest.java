package com.demo.server.common.util;

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
	public void rateLimitTest() {
		String url = "http://127.0.0.1:6673/test/rate/test";

		int threadNum = 4;
		int loopInThreadSize = 100;

		long startTime = System.currentTimeMillis();

		System.out.println(">>> Task start at " + DateUtil.getCurrentDateTimeStr());

		CountDownLatch countDownLatch = new CountDownLatch(threadNum);

		for (int i = 0; i < threadNum; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					long start = System.currentTimeMillis();
					int currentLoop = 0;

					System.out.printf("%s run loop start. %n", Thread.currentThread().getName(), currentLoop);

					while (currentLoop < loopInThreadSize) {
						currentLoop++;
						try {
							Response response = Jsoup.connect(url).ignoreContentType(true).execute();
							String jsonStr = response.body();

							JSONObject jsonObject = JSON.parseObject(jsonStr);
							int code = jsonObject.getInteger("code");
							if (code == 0) {
								successCount.addAndGet(1);
							} else {
								rateLimitCount.addAndGet(1);
								// System.out.println("limit response: " + jsonObject.toJSONString());
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					long end = System.currentTimeMillis();

					System.out.printf("%s run [%d] loop done. cost [%d] ms %n", Thread.currentThread().getName(),
							currentLoop, (end - start));

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
		long successNum = successCount.intValue();
		long limitNum = rateLimitCount.intValue();

		long totalCost = endTime - startTime;
		double successRate = successNum <= 0 ? 0 : Double.valueOf(successNum) / (limitNum + successNum);
		double throughput = Double.valueOf(limitNum + successNum) / (totalCost / 1000d);

		System.out.println(">>> Task end at " + DateUtil.getCurrentDateTimeStr());
		System.out.println("cost: " + totalCost + "ms");
		System.out.println("throughput: " + String.format("%.2f", throughput) + "/sec");
		System.out.println("successCount: " + successNum);
		System.out.println("limitCount: " + limitNum);
		System.out.println("successRate: " + String.format("%.2f", successRate * 100) + "%");

	}

}
