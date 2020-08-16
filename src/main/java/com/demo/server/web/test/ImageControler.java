package com.demo.server.web.test;

import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/test/image")
public class ImageControler {
    @TokenPass
    @SignPass
    @GetMapping("/test")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(name = "id", required = false) String id) {
        try (OutputStream os = response.getOutputStream()) {
            BufferedImage image = ImageIO.read(new FileInputStream(new File("./tmp/readFileFromUrl.png")));
            response.setContentType("image/png;charset=UTF-8");
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
