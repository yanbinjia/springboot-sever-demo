package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.exception.AppException;
import com.demo.server.common.interceptor.TokenPass;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/hystrix")
@Slf4j
public class TestHystrixControler {
    @TokenPass
    @GetMapping("/test")
    @HystrixCommand(fallbackMethod = "getTestFallback")
    public Result<String> test() {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        result.setData("RateLimit test ok.");
        throw new AppException("TestHystrix");
    }

    public Result<String> getTestFallback(Integer id, Throwable throwable) {
        Result<String> result = new Result<>(ResultCode.FAILED);
        result.setExtMsg(throwable.getMessage());
        return result;
    }

}
