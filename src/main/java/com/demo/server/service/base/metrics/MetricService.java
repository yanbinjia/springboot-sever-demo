package com.demo.server.service.base.metrics;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;

@Component
@Slf4j
public class MetricService {

    public static final String LIMIT_KEY_SPLIT = "->";

    private static int queueSize = 10000;

    private static int threadNum = 5;

    private static ExecutorService executor = Executors.newFixedThreadPool(threadNum);

    private static final MetricRegistry metricRegistry = new MetricRegistry();

    private static final BlockingQueue<MetricEvent> METRIC_EVENT_QUEUE = new LinkedBlockingDeque<>(queueSize);

    public static boolean putMetricEvent(MetricEvent metircData) {
        return MetricService.METRIC_EVENT_QUEUE.offer(metircData);
    }

    public static List<MetricBean> getAllMetrics() {
        return MetricUtil.toTimerList(metricRegistry.getTimers());
    }

    @PostConstruct
    public void init() {
        log.info(">>> Init MetricService.");
        for (int i = 0; i < threadNum; i++) {
            int finalI = i + 1;
            MetricService.executor.submit(new Runnable() {
                @Override
                public void run() {
                    log.info(">>> MetricService thread[{}] start.", finalI);
                    String metricKey = "";
                    Timer timer = null;
                    while (true) {
                        try {
                            MetricEvent event = MetricService.METRIC_EVENT_QUEUE.take();
                            if (event != null) {
                                metricKey = event.getMetricKey();
                                if (StringUtil.isNotBlank(metricKey)) {
                                    timer = metricRegistry.timer(metricKey);
                                    timer.update(event.getCost(), TimeUnit.MILLISECONDS);
                                    log.debug(">>> One MetricEvent write to timer ok.");
                                }

                            }
                        } catch (InterruptedException e) {
                            log.error("InterruptedException", e);
                        }
                    }
                }
            });
        }
    }


}
