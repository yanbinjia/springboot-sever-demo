package com.demo.server.service.base.metrics;

import lombok.Data;

@Data
public class MetricBean {
    private String method;
    private String count = "";
    private String m1_rate = "";
    private String m5_rate = "";
    private String m15_rate = "";
    private String mean_rate = "";

    private String min = "";
    private String max = "";
    private String mean = "";
    private String stddev = "";

    private String p50 = "";
    private String p75 = "";
    private String p95 = "";
    private String p98 = "";
    private String p99 = "";
    private String p999 = "";
}
