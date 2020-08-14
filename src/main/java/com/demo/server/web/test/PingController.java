/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-14T15:18:54.432+08:00
 */

package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
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

}
