package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/test/xss")
@Validated
public class XssTestController {
    @TokenPass
    @SignPass
    @GetMapping("/test")
    @ResponseBody
    public Result<String> xssTest(
            @RequestParam Long id,
            @NotEmpty(message = "不能为空")
            @RequestParam String value) {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        String idStr = String.valueOf(id);
        String valueStr = value;
        result.setData(valueStr);
        return result;
    }
}
