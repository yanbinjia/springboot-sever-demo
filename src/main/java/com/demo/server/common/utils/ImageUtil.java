package com.demo.server.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImageUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    private static final List<String> IMAGE_EXT_LIST = Arrays.asList("bmp", "gif", "jpg", "jpeg", "png");

    public static boolean isImageByIO(File file) {
        boolean result = false;

        if (file == null || !file.exists()) {
            return false;
        }

        BufferedImage image = null;

        try {
            image = ImageIO.read(file);
            if (image != null && image.getWidth() > 0 && image.getHeight() > 0) {
                result = true;
            }
        } catch (Exception e) {
            logger.error("isImage error", e);
        }

        return result;
    }

    public static boolean isImageByExt(String filename) {
        return IMAGE_EXT_LIST.contains(getExt(filename).toLowerCase());
    }

    public static String getExt(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static boolean getFromUrl(String url, String filePath) {
        if (StringUtils.isAnyBlank(url, filePath)) {
            return false;
        }

        boolean result = false;

        try (FileOutputStream out = new FileOutputStream(new File(filePath))) {
            Response response = Jsoup.connect(url).ignoreContentType(true).execute();
            out.write(response.bodyAsBytes());
            out.flush();
            result = true;
        } catch (IOException e) {
            logger.error("getFileFromUrl error. url={},path={}", url, filePath, e);
        }

        return result;
    }

    public static BufferedImage zoomByRatio(String filePath, int width, int height, double quality) throws IOException {
        return Thumbnails.of(filePath).size(width, height).outputQuality(quality).asBufferedImage();
    }

    public static BufferedImage zoomByRatio(String filePath, double scale, double quality) throws IOException {
        return Thumbnails.of(filePath).scale(scale).outputQuality(quality).asBufferedImage();
    }

    public static BufferedImage zoomBySize(String filePath, int width, int height, double quality) throws IOException {
        return Thumbnails.of(filePath).size(width, height).outputQuality(quality).keepAspectRatio(false).asBufferedImage();
    }

    public static BufferedImage rotate(String filePath, int width, int height, double angle) throws IOException {
        return Thumbnails.of(filePath).size(width, height).rotate(angle).asBufferedImage();
    }

    public static BufferedImage watermark(String filePath, String text, int fontSize, Color color) throws IOException {
        Graphics2D graphics = null;
        BufferedImage watermarkImage = null;
        try {
            Font font = new Font("", Font.BOLD, fontSize);
            FontMetrics fm = new JLabel(text).getFontMetrics(font);

            int extraSize = fontSize / 12;// 预留多出一点宽度和高度
            int width = fm.charsWidth(text.toCharArray(), 0, text.length()) + extraSize; // 所有设置字体的字符总宽度
            int height = fontSize + extraSize;// 高度

            logger.debug("fontSize={},extraSize={},watermarkImage(水印画布) width={},height={}", fontSize, extraSize, width, height);

            watermarkImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            graphics = watermarkImage.createGraphics();

            // 设置为背景透明
            watermarkImage = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            graphics.dispose();

            // 绘制字符串
            graphics = watermarkImage.createGraphics();

            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setClip(0, 0, width, height);

            graphics.setFont(font);
            graphics.setColor(color);

            Rectangle rectangle = graphics.getClipBounds();
            FontMetrics fontMetrics = graphics.getFontMetrics(font);
            int ascent = fontMetrics.getAscent();
            int decent = fontMetrics.getDescent();

            int x = extraSize / 2;
            int y = (rectangle.height - (ascent + decent)) / 2 + ascent;
            // int y = height - extraSize; // 可用但不完美.

            graphics.drawString(text, x, y);

        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }

        return Thumbnails.of(filePath).scale(1f).watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.70f).asBufferedImage();
    }

    public static boolean saveToFile(BufferedImage bufferedImage, String formatName, String filePath) {
        if (bufferedImage == null || StringUtils.isBlank(filePath)) {
            logger.error("saveToPath error. qrBuffImg,filePath cannot be null.");
            return false;
        }
        try {
            ImageIO.write(bufferedImage, formatName, new File(filePath));
            return true;
        } catch (IOException e) {
            logger.error("saveToPath error. filePath=[{}]", filePath, e);
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(ImageUtil.isImageByExt("/sf/sf/saf/ddd.txt.png"));
        String filePath = "./tmp/readFileFromUrl.png";
        String url = "https://www.baidu.com/img/flexible/logo/pc/result@2.png";
        ImageUtil.getFromUrl(url, filePath);

        String srcFilePath = "./tmp/src_img.jpg";
        BufferedImage bufferedImage = ImageUtil.zoomByRatio(srcFilePath, 0.2, 0.9);
        ImageUtil.saveToFile(bufferedImage, "jpg", "./tmp/src_img_0.2.jpg");

        bufferedImage = ImageUtil.watermark(srcFilePath, "Power by demo. @balabalabala.", 30, Color.lightGray);
        ImageUtil.saveToFile(bufferedImage, "jpg", "./tmp/src_img_watermark.jpg");
    }
}
