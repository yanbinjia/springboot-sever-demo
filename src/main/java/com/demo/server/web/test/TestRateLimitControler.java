package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.common.interceptor.aspect.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/rate")
public class TestRateLimitControler {
    @TokenPass
    @GetMapping("/test")
    @RateLimit(qps = 100, timeout = 100)
    public Result<String> test() {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        result.setData("RateLimit test ok.");
        return result;
    }

}
