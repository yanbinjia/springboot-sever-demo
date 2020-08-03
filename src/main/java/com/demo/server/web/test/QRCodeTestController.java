package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.common.utils.qrcode.ARGBColor;
import com.demo.server.common.utils.qrcode.QRCodeUtil;
import com.demo.server.common.utils.system.JvmUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/qrcode")
@Validated
public class QRCodeTestController {

    @TokenPass
    @SignPass
    @GetMapping("/test")
    @ResponseBody
    public Result<Map<String, String>> test(@NotEmpty(message = "不能为空") @RequestParam String content) {
        String logoPath = "";
        String backgroundPath = "";

        Result<Map<String, String>> result = new Result<>(ResultCode.SUCCESS);
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("image", QRCodeUtil.genQRCodeImgBase64(content, 300, 300, ARGBColor.Black, ARGBColor.White, logoPath, backgroundPath));
        result.setData(map);

        return result;
    }

    @TokenPass
    @SignPass
    @GetMapping("/test2")
    public void testOutput1(HttpServletResponse response,
                            @NotEmpty(message = "不能为空") @RequestParam String content) {
        BufferedImage bufferedImage = QRCodeUtil.genQRCodeImg(content, 300, 300);
        QRCodeUtil.outToWeb(response, content, bufferedImage);
    }

    @TokenPass
    @SignPass
    @GetMapping("/test3")
    public void testOutput2(HttpServletResponse response,
                            @NotEmpty(message = "不能为空") @RequestParam String content) {
        String logoPath = JvmUtil.getClassPathRoot() + "/resources/logo-wechat.png";
        BufferedImage bufferedImage = QRCodeUtil.genQRCodeImg(content, 300, 300, ARGBColor.DoderBlue, ARGBColor.White, logoPath, null);
        QRCodeUtil.outToWeb(response, content, bufferedImage);
    }
}
