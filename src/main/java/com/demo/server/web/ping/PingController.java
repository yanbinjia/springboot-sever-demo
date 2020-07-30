package com.demo.server.web.ping;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.exception.AppException;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.common.utils.RandomUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PingController {

    @SignPass
    @TokenPass
    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "pong";
    }

    @SignPass
    @TokenPass
    @GetMapping("/ping2")
    @ResponseBody
    public Result<String> ping2() {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        result.setData("pong");
        return result;
    }

    @SignPass
    @TokenPass
    @GetMapping("/ping3")
    @ResponseBody
    public Result<String> ping3() {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        result.setData("pong3");
        try {
            Thread.sleep(RandomUtil.randomInt(1000, 2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @SignPass
    @TokenPass
    @GetMapping("/pingerror")
    @ResponseBody
    public Result<String> pingError() {
        // Controller异常捕获和日志记录
        throw new AppException("pingerror for test.");

    }

}
