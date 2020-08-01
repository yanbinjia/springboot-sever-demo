/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-31T21:00:12.328+08:00
 */

package com.demo.server.common.utils.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Hashtable;

public class QRCodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);

    public static final String IMG_FORMAT_PNG = "png";

    public static BufferedImage genQrCodeImg(String content, int width, int height) {
        return genQrCodeImg(content, width, height, ARGBColor.white, null, null);
    }

    public static String genQrCodeImgBase64(String content, int width, int height) {
        return toBase64(genQrCodeImg(content, width, height));
    }

    public static String genQrCodeImgBase64(String content, int width, int height, int backgroundColor,
                                            String logoPath, String backgroundPath) {
        BufferedImage bufferedImage = genQrCodeImg(content, width, height, backgroundColor, logoPath, backgroundPath);
        return toBase64(bufferedImage);
    }

    public static BufferedImage genQrCodeImg(String content, int width, int height, int backgroundColor,
                                             String logoPath, String backgroundPath) {
        BufferedImage bufferedImage = null;
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            // 设置quietZoneInt,留白
            hints.put(EncodeHintType.MARGIN, 0);
            // ERROR_CORRECTION
            // (7%-30%),容错率越高,二维码的有效像素点就越多,参考ErrorCorrectionLevel
            // ERROR_CORRECTION (默认为L:7%),二维码图片中间加logo,为提高识别率,需提高容错率设置
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

            BitMatrix bitMatrix = new CustomMultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);

            bufferedImage = writeBufferedImage(bitMatrix, backgroundColor, logoPath, backgroundPath);

        } catch (Exception e) {
            logger.error("genQrCodeImg error:", e);
        }

        return bufferedImage;
    }

    public static BufferedImage writeBufferedImage(BitMatrix matrix, int backgroundColor,
                                                   String logoPath, String backgroundPath) {
        BufferedImage bufferedImage = toBasicBufferedImage(matrix, backgroundColor);

        if (StringUtils.isNotBlank(logoPath)) {
            bufferedImage = addLogo(bufferedImage, logoPath);
        }

        if (StringUtils.isNotBlank(backgroundPath)) {
            bufferedImage = addBackground(bufferedImage, backgroundPath);
        }
        return bufferedImage;
    }

    public static BufferedImage toBasicBufferedImage(BitMatrix matrix, int backgroundColor) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, matrix.get(x, y) ? ARGBColor.black : backgroundColor);
            }
        }
        return bufferedImage;
    }

    public static BufferedImage addLogo(BufferedImage bufferedImage, String logoPath) {
        File logoFile = new File(logoPath);
        if (!logoFile.exists()) {
            return bufferedImage;
        }

        Graphics graphics = null;
        try {
            graphics = bufferedImage.getGraphics();
            BufferedImage logoBuffImg = ImageIO.read(logoFile);

            int logoWidth = logoBuffImg.getWidth();
            int logoHeight = logoBuffImg.getHeight();

            // 计算图片放置位置
            int x = (bufferedImage.getWidth() - logoWidth) / 2;
            int y = (bufferedImage.getHeight() - logoBuffImg.getHeight()) / 2;

            // 开始绘制图片
            // graphics.drawRoundRect(x, y, logoWidth, logoHeight, 10, 10);
            // graphics.drawRect(x, y, logoWidth, logoHeight);
            graphics.drawImage(logoBuffImg, x, y, logoWidth, logoHeight, null);
        } catch (IOException e) {
            logger.error("addLogo error.", e);
        } finally {
            // 释放资源
            graphics.dispose();
        }

        return bufferedImage;
    }

    public static BufferedImage addBackground(BufferedImage bufferedImage, String backgroundPath) {
        File backgroundFile = new File(backgroundPath);
        if (!backgroundFile.exists()) {
            return bufferedImage;
        }

        BufferedImage backgroundImg = null;
        Graphics graphics = null;
        try {
            backgroundImg = ImageIO.read(backgroundFile);
            graphics = backgroundImg.getGraphics();
            // 计算插入位置
            int x = backgroundImg.getWidth() / 2 - bufferedImage.getWidth(null) / 2;
            int y = backgroundImg.getHeight() / 7;
            // 绘制
            graphics.drawImage(bufferedImage, x, y, null);
        } catch (IOException e) {
            logger.error("addBackground error.", e);
        } finally {
            graphics.dispose();
        }

        return bufferedImage;
    }

    public static boolean saveToPath(BufferedImage bufferedImage, String outputPath) {
        if (bufferedImage == null || StringUtils.isBlank(outputPath)) {
            logger.error("saveToPath error. qrBuffImg,outputPath cannot be null.");
            return false;
        }
        try {
            ImageIO.write(bufferedImage, IMG_FORMAT_PNG, new File(outputPath));
            return true;
        } catch (IOException e) {
            logger.error("saveToPath error. outputPath=[{}]", outputPath, e);
        }
        return false;
    }

    public static String toBase64(BufferedImage bufferedImage) {
        String type = "data:image/png;base64,";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, IMG_FORMAT_PNG, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("toBase64 error.", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                logger.error("toBase64 error.", e);
            }
        }
        return type + Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public static void setHeader(HttpServletResponse response, String content) {
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("QRCodeContent", content);// content
    }

    public static boolean output(HttpServletResponse response, String content, BufferedImage bufferedImage) {
        setHeader(response, content);
        return output(response, bufferedImage);
    }

    public static boolean output(HttpServletResponse response, BufferedImage bufferedImage) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(bufferedImage, IMG_FORMAT_PNG, out);
            out.flush();
            return true;
        } catch (IOException e) {
            logger.error("output error.", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("output error.", e);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String content = "https://mp.weixin.qq.com/xxxxxxxxxxx";
        String logoPath = "./tmp/1.png";
        String backgroundPath = "./tmp/2.png";
        String dstPath = "./tmp/qrcode.png";
        int backgroundColor = 0xFFb5e871;

        BufferedImage bufferedImage = QRCodeUtil.genQrCodeImg(content, 300, 300, ARGBColor.white, logoPath, backgroundPath);

        boolean success = QRCodeUtil.saveToPath(bufferedImage, dstPath);

        System.out.printf("saveToPath success=[%s] dstPath=[%s] %n", success, dstPath);
        System.out.println("base64:");
        System.out.println(QRCodeUtil.toBase64(bufferedImage));
    }
}