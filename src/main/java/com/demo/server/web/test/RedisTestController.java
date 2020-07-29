package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.bean.entity.UserInfo;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.service.base.cache.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/redis")
@Validated
public class RedisTestController {
    @Autowired
    RedisService redisService;

    @TokenPass
    @SignPass
    @GetMapping("/test")
    @ResponseBody
    public Result<String> test(
            @RequestParam(name = "id", required = true) Long id,
            @NotEmpty(message = "不能为空")
            @RequestParam String value) {

        Result<String> result = new Result<>(ResultCode.SUCCESS);
        String idStr = String.valueOf(id);
        String valueStr = value;

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@mail.com");
        userInfo.setId(123L);
        userInfo.setMobile("113466799355");
        userInfo.setUserName(idStr);
        userInfo.setOtherInfo(valueStr);
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());

        redisService.set(idStr, userInfo, -1L);

        redisService.set("123456", "1234567String", -1L);


        result.setData(String.valueOf(redisService.get(idStr)));
        result.setMsg(String.valueOf(redisService.get("123456")));

        return result;
    }
}
