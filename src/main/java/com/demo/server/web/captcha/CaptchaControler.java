package com.demo.server.web.captcha;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.common.utils.RandomUtil;
import com.demo.server.common.utils.captcha.easycaptcha.ArithmeticCaptcha;
import com.demo.server.common.utils.captcha.easycaptcha.GifCaptcha;
import com.demo.server.common.utils.captcha.easycaptcha.SpecCaptcha;
import com.demo.server.common.utils.captcha.easycaptcha.base.Captcha;
import com.demo.server.common.utils.captcha.easycaptcha.utils.CaptchaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/captcha")
@Slf4j
public class CaptchaControler {
    @TokenPass
    @SignPass
    @GetMapping("/test1")
    public void test1(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 使用gif验证码
            GifCaptcha gifCaptcha = new GifCaptcha(130, 48, 5);
            CaptchaUtil.out(gifCaptcha, request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TokenPass
    @SignPass
    @GetMapping("/test2")
    public void test2(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 设置宽、高、位数
            CaptchaUtil.out(130, 48, 5, request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TokenPass
    @SignPass
    @GetMapping("/test3")
    public void test3(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 三个参数分别为宽、高、位数
            SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);
            // 设置字体
            captcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置

            // 使用内置字体
            // captcha.setFont(Captcha.FONT_1);

            // 设置类型，纯数字、纯字母、字母数字混合
            captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

            String verifyCode = captcha.text();
            String captchaKey = RandomUtil.uuidWithoutSeparator();

            log.debug("verifyCode=[{}]", verifyCode);

            CaptchaUtil.setHeader(response, captchaKey);

            // 输出图片流
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TokenPass
    @SignPass
    @GetMapping("/test4")
    public void test4(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 三个参数分别为宽、高、位数
            SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);

            // 设置类型，纯数字、纯字母、字母数字混合
            captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

            String verifyCode = captcha.text();
            String captchaKey = RandomUtil.uuidWithoutSeparator();

            log.debug("verifyCode=[{}]", verifyCode);

            CaptchaUtil.setHeader(response, captchaKey);

            // 输出图片流
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @TokenPass
    @SignPass
    @GetMapping("/test5")
    public void test5(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 算术类型
            ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
            captcha.setLen(3);  // 几位数运算，默认是两位
            captcha.text();  // 获取运算的结果：5

            String verifyCode = captcha.text();
            String captchaKey = RandomUtil.uuidWithoutSeparator();

            log.debug("verifyCode=[{}]", verifyCode);

            CaptchaUtil.setHeader(response, captchaKey);

            // 输出图片流
            captcha.out(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TokenPass
    @SignPass
    @GetMapping("/test6")
    public Result<Map<String, String>> test6(HttpServletRequest request, HttpServletResponse response) {

        SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);

        String verifyCode = captcha.text();
        String captchaKey = RandomUtil.uuidWithoutSeparator();

        Result<Map<String, String>> result = new Result<>(ResultCode.SUCCESS);
        Map<String, String> map = new HashMap<>();
        map.put("key", captchaKey);// 与验证码一起提交服务端,用于验证匹配
        map.put("image", captcha.toBase64());
        result.setData(map);

        log.debug("verifyCode=[{}]", verifyCode);

        return result;
    }


}
