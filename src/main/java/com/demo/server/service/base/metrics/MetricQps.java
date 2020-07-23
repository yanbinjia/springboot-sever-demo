/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-23T18:56:53.766+08:00
 */

package com.demo.server.service.base.metrics;

import lombok.Data;

@Data
public class MetricQps {
    private String m1_rate;
    private String m5_rate;
    private String m15_rate;
    private String mean_rate;
}
