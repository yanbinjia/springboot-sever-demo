/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-31T21:00:12.328+08:00
 */

package com.demo.server.common.utils.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class QRCodeUtil {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);

    public static final String IMG_FORMAT_PNG = "png";

    public static BufferedImage genQRCodeImg(String content, int width, int height) {
        return genQRCodeImg(content, width, height, ARGBColor.Black, ARGBColor.White, null, null);
    }

    public static BufferedImage genQRCodeImg(String content, int width, int height,
                                             int lineColor, int backgroundColor) {
        return genQRCodeImg(content, width, height, lineColor, backgroundColor, null, null);
    }

    public static String genQRCodeImgBase64(String content, int width, int height,
                                            int lineColor, int backgroundColor,
                                            String logoPath, String backgroundPath) {
        BufferedImage bufferedImage = genQRCodeImg(content, width, height, lineColor, backgroundColor, logoPath, backgroundPath);
        return toBase64(bufferedImage);
    }

    public static BufferedImage genQRCodeImg(String content, int width, int height,
                                             int lineColor, int backgroundColor,
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

            bufferedImage = drawQRCodeImg(bitMatrix, lineColor, backgroundColor, logoPath, backgroundPath);

        } catch (Exception e) {
            logger.error("genQrCodeImg error:", e);
        }

        return bufferedImage;
    }

    public static BufferedImage drawQRCodeImg(BitMatrix matrix, int lineColor, int backgroundColor,
                                              String logoPath, String backgroundPath) {
        BufferedImage bufferedImage = drawBasicQRCodeImg(matrix, lineColor, backgroundColor);

        if (StringUtils.isNotBlank(logoPath)) {
            addLogo(bufferedImage, logoPath);
        }

        if (StringUtils.isNotBlank(backgroundPath)) {
            addBackground(bufferedImage, backgroundPath);
        }
        return bufferedImage;
    }

    public static BufferedImage drawBasicQRCodeImg(BitMatrix matrix, int lineColor, int backgroundColor) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, matrix.get(x, y) ? lineColor : backgroundColor);
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

            // 计算设置logo放置位置,二维码正中间
            int x = bufferedImage.getWidth() / 5 * 2;
            int y = bufferedImage.getHeight() / 5 * 2;
            // 计算设置logo大小,根据需求调整
            int logoWidth = bufferedImage.getHeight() / 5;
            int logoHeight = bufferedImage.getHeight() / 5;
            // 绘制logo到二维码
            graphics.drawImage(logoBuffImg, x, y, logoWidth, logoHeight, null);

            // 美化,绘制指定弧度的圆角矩形
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics2D.setColor(Color.white);
            graphics2D.drawRoundRect(x, y, logoWidth, logoHeight, 20, 20);

            /*
            Graphics2D graphics2D = (Graphics2D) graphics;
            // 画白色边装饰
            BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            graphics2D.setStroke(stroke);// 设置笔画对象
            //指定弧度的圆角矩形
            RoundRectangle2D.Float round = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 20, 20);
            graphics2D.setColor(Color.white);
            graphics2D.draw(round);// 绘制圆弧矩形

            //画logo外侧灰色边框
            BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            graphics2D.setStroke(stroke2);// 设置笔画对象
            RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(x + 2, y + 2, logoWidth - 4, logoHeight - 4, 20, 20);
            graphics2D.setColor(Color.gray);
            graphics2D.draw(round2);// 绘制圆弧矩形
             */

        } catch (IOException e) {
            logger.error("addLogo error.", e);
        } finally {
            if (graphics != null) {
                graphics.dispose();// 释放资源
            }
        }

        return bufferedImage;
    }

    public static BufferedImage addBackground(BufferedImage bufferedImage, String backgroundPath) {
        File backgroundFile = new File(backgroundPath);
        if (!backgroundFile.exists()) {
            return bufferedImage;
        }

        Graphics graphics = null;
        try {
            BufferedImage backgroundImg = ImageIO.read(backgroundFile);
            graphics = backgroundImg.getGraphics();
            // 计算插入位置
            int x = backgroundImg.getWidth() / 2 - bufferedImage.getWidth(null) / 2;
            int y = backgroundImg.getHeight() / 7;
            // 绘制
            graphics.drawImage(bufferedImage, x, y, null);
        } catch (IOException e) {
            logger.error("addBackground error.", e);
        } finally {
            if (graphics != null) {
                graphics.dispose();// 释放资源
            }
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

    public static boolean outToWeb(HttpServletResponse response, String content, BufferedImage bufferedImage) {
        setHeader(response, content);
        return outToWeb(response, bufferedImage);
    }

    public static boolean outToWeb(HttpServletResponse response, BufferedImage bufferedImage) {
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
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("output error.", e);
            }
        }
        return false;
    }

    public static String decode(String filePath) {
        String content = null;
        File qrFile = new File(filePath);
        if (StringUtils.isBlank(filePath) || !qrFile.exists()) {
            return null;
        }
        try {
            content = decode(ImageIO.read(qrFile));
        } catch (IOException e) {
            logger.error("decode error.", e);
        }
        return content;
    }

    public static String decode(BufferedImage bufferedImage) {
        String content = null;
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            if (result != null) {
                content = result.getText();
            }
        } catch (Exception e) {
            logger.error("decode error.", e);
        }
        return content;
    }

    public static void main(String[] args) {
        String content = "https://www.baidu.com/s?wd=qrcode%20zxing";
        String logoPath = "./doc/resources/logo-transformers.png";
        String logoPathOfWechat = "./doc/resources/logo-wechat.png";
        String logoPathOfChrome = "./doc/resources/logo-chrome.png";
        String backgroundPath = "./doc/resources/2.png";
        String dstPath = "./tmp/";

        int width = 350;
        int height = 350;

        BufferedImage bufferedImage1 = QRCodeUtil.genQRCodeImg(content, width, height, ARGBColor.RoyalBlue, ARGBColor.White, logoPathOfWechat, backgroundPath);
        BufferedImage bufferedImage2 = QRCodeUtil.genQRCodeImg(content, width, height, ARGBColor.DoderBlue, ARGBColor.White, logoPathOfChrome, backgroundPath);

        BufferedImage bufferedImage3 = QRCodeUtil.genQRCodeImg(content, width, height, ARGBColor.SeaGreen, ARGBColor.White, logoPath, backgroundPath);
        BufferedImage bufferedImage4 = QRCodeUtil.genQRCodeImg(content, width, height, ARGBColor.Purple2, ARGBColor.White, logoPath, backgroundPath);

        QRCodeUtil.saveToPath(bufferedImage1, dstPath + "qrcode1.png");
        QRCodeUtil.saveToPath(bufferedImage2, dstPath + "qrcode2.png");
        QRCodeUtil.saveToPath(bufferedImage3, dstPath + "qrcode3.png");
        QRCodeUtil.saveToPath(bufferedImage4, dstPath + "qrcode4.png");

        System.out.println("decode(buffImg)=" + QRCodeUtil.decode(bufferedImage1));
        System.out.println("decode(filePath)=" + QRCodeUtil.decode(dstPath + "qrcode1.png"));

    }
}