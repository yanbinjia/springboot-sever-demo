package com.demo.server.web.captcha;

import com.demo.server.common.captcha.easycaptcha.ArithmeticCaptcha;
import com.demo.server.common.captcha.easycaptcha.GifCaptcha;
import com.demo.server.common.captcha.easycaptcha.SpecCaptcha;
import com.demo.server.common.captcha.easycaptcha.base.Captcha;
import com.demo.server.common.captcha.easycaptcha.utils.CaptchaUtil;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
            // 设置请求头为输出图片类型
            response.setContentType("image/gif");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            // 三个参数分别为宽、高、位数
            SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);
            // 设置字体
            captcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置

            // 使用内置字体
            // captcha.setFont(Captcha.FONT_1);

            // 设置类型，纯数字、纯字母、字母数字混合
            captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

            // 验证码图片的字符
            String captchaText = captcha.text();

            log.debug("captchaText=[{}]", captchaText);

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
            // 设置请求头为输出图片类型
            response.setContentType("image/gif");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            // 算术类型
            ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
            captcha.setLen(3);  // 几位数运算，默认是两位
            captcha.text();  // 获取运算的结果：5

            // 验证码图片的字符
            String captchaText = captcha.text();

            log.debug("captchaText=[{}]", captchaText);

            // 输出图片流
            captcha.out(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
