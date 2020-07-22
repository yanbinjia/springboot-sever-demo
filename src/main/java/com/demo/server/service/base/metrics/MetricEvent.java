package com.demo.server.service.base.metrics;

import lombok.Data;

@Data
public class MetricEvent {
    private EventType eventType;
    private long cost;
    private String metricKey;

}
