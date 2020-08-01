package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.bean.entity.UserInfo;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.common.utils.RandomUtil;
import com.demo.server.service.base.cache.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

        Result<String> result = new Result<>(ResultCode.SUCCESS);
        result.setData(String.valueOf(redisService.get(idStr)));
        result.setMsg(String.valueOf(redisService.get("123456")));

        return result;
    }

    @TokenPass
    @SignPass
    @GetMapping("/bitmap")
    @ResponseBody
    public Result<Map<String, String>> bitmap(@RequestParam(name = "id", required = true) Long id) {
        String key = "online";
        redisService.setBit(key, id, true);
        boolean getbit = redisService.getBit(key, id);

        Result<Map<String, String>> result = new Result<>(ResultCode.SUCCESS);
        Map<String, String> map = new HashMap<>();
        map.put("bitmap-key", key);
        map.put("bitcount", String.valueOf(redisService.bitCount(key)));
        map.put("getbit[" + id + "]", String.valueOf(getbit));
        result.setData(map);

        return result;
    }

    @TokenPass
    @SignPass
    @GetMapping("/lock")
    @ResponseBody
    public Result<Map<String, String>> lock(@RequestParam(required = true) @NotEmpty(message = "不能为空") String key) {

        String lockKey = RedisService.LOCK_PREFIX + key.trim();
        String lockValue = lockKey + "@" + RandomUtil.uuidWithoutSeparator();
        int expireSeconds = 60 * 5;

        boolean getLock = redisService.getLock(lockKey, lockValue, expireSeconds);

        Result<Map<String, String>> result = new Result<>(ResultCode.SUCCESS);
        Map<String, String> map = new HashMap<>();

        if (getLock) {
            map.put("getLock", "success");
            map.put("lockKey", lockKey);
            map.put("lockValue", String.valueOf(redisService.get(lockKey)));
            map.put("expireSeconds", expireSeconds + "");
        } else {
            map.put("getLock", "fail");
            map.put("lockKey", lockKey);
            map.put("lockValue", String.valueOf(redisService.get(lockKey)));
            map.put("expireSeconds", expireSeconds + "");
        }

        result.setData(map);

        return result;
    }

    @TokenPass
    @SignPass
    @GetMapping("/lock2")
    @ResponseBody
    public Result<Map<String, String>> lock(@RequestParam(required = true)
                                            @NotEmpty(message = "不能为空") String key,
                                            @RequestParam(required = true)
                                            @NotEmpty(message = "不能为空") String value) {

        String lockKey = RedisService.LOCK_PREFIX + key.trim();
        String lockValue = value;
        int expireSeconds = 60 * 5;

        boolean getLock = redisService.getLock(lockKey, lockValue, expireSeconds);

        Result<Map<String, String>> result = new Result<>(ResultCode.SUCCESS);
        Map<String, String> map = new HashMap<>();

        if (getLock) {
            map.put("getLock", "success");
            map.put("lockKey", lockKey);
            map.put("lockValue", String.valueOf(redisService.get(lockKey)));
            map.put("expireSeconds", expireSeconds + "");
        } else {
            map.put("getLock", "fail");
            map.put("lockKey", lockKey);
            map.put("lockValue", String.valueOf(redisService.get(lockKey)));
            map.put("expireSeconds", expireSeconds + "");
        }

        result.setData(map);

        return result;
    }

    @TokenPass
    @SignPass
    @GetMapping("/releaselock")
    @ResponseBody
    public Result<Map<String, String>> releaseLock(@RequestParam(required = true) @NotEmpty(message = "不能为空") String key,
                                                   @RequestParam(required = true) @NotEmpty(message = "不能为空") String value) {

        String lockKey = RedisService.LOCK_PREFIX + key.trim();
        String lockValue = value;

        boolean releaseLock = redisService.releaseLock(lockKey, lockValue);

        Result<Map<String, String>> result = new Result<>(ResultCode.SUCCESS);
        Map<String, String> map = new HashMap<>();

        if (releaseLock) {
            map.put("releaseLock", "success");
            map.put("lockKey", lockKey);
            map.put("lockValue", lockValue);
        } else {
            map.put("releaseLock", "fail");
            map.put("lockKey", lockKey);
            map.put("lockValue", String.valueOf(redisService.get(lockKey)));
        }

        result.setData(map);

        return result;
    }
}
