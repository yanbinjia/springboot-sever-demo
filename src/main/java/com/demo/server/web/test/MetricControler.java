/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-14T15:13:33.822+08:00
 */

package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.common.utils.system.JvmInfo;
import com.demo.server.common.utils.system.JvmUtil;
import com.demo.server.common.utils.system.OshiUtil;
import com.demo.server.service.base.metrics.Metric;
import com.demo.server.service.base.metrics.MetricService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test/metric")
public class MetricControler {
    @TokenPass
    @SignPass
    @GetMapping("/metrics")
    public Result<List<Metric>> metrics() {
        Result<List<Metric>> result = new Result<>(ResultCode.SUCCESS);
        result.setData(MetricService.getAllMetrics());
        return result;
    }

    @TokenPass
    @SignPass
    @GetMapping("/osinfo")
    public Result<Map<String, Object>> osinfo() {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        Map<String, Object> map = new HashMap<>();
        map.put("CpuInfo", OshiUtil.getCpuInfo());
        map.put("Memory", OshiUtil.getMemory());
        map.put("System", OshiUtil.getSystem());
        map.put("NetworkIFs", OshiUtil.getNetworkIFs());
        result.setData(map);
        return result;
    }

    @TokenPass
    @SignPass
    @GetMapping("/jvminfo")
    public Result<JvmInfo> jvminfo() {
        Result<JvmInfo> result = new Result<>(ResultCode.SUCCESS);
        result.setData(JvmUtil.getJvmInfo());
        return result;
    }

}
