package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.bean.entity.UserInfo;
import com.demo.server.bean.vo.UserInfoParam;
import com.demo.server.bean.vo.UserInfoResult;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.service.user.UserInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@RestController
@RequestMapping("/xss")
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
