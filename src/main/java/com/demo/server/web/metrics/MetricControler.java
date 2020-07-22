package com.demo.server.web.metrics;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.service.base.metrics.MetricBean;
import com.demo.server.service.base.metrics.MetricService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/metric")
public class MetricControler {
    @TokenPass
    @SignPass
    @GetMapping("/metrics")
    public Result<List<MetricBean>> metrics() {

        Result<List<MetricBean>> result = new Result<>(ResultCode.SUCCESS);
        result.setData(MetricService.getAllMetrics());

        return result;
    }

}
