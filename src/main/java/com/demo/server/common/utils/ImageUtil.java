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
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public static final String FMT_PNG = "png";
    public static final String FMT_GIF = "gif";
    public static final String FMT_JPG = "jpg";
    public static final String FMT_JPEG = "jpeg";
    public static final String FMT_BMP = "bmp";
    public static final String FMT_ICO = "ico";

    public static final List<String> IMG_EXT_LIST = Arrays.asList(FMT_PNG, FMT_GIF, FMT_JPG, FMT_JPEG, FMT_BMP, FMT_ICO);

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
        return IMG_EXT_LIST.contains(getExt(filename).toLowerCase());
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

    /**
     * 按大小,等比例缩放
     */
    public static BufferedImage zoomByRatio(String filePath, int width, int height, double quality) throws IOException {
        return Thumbnails.of(filePath).size(width, height).outputQuality(quality).asBufferedImage();
    }

    /**
     * 按大小,等比例缩放
     */
    public static BufferedImage zoomByRatio(BufferedImage bufferedImage, int width, int height, double quality) throws IOException {
        return Thumbnails.of(bufferedImage).size(width, height).outputQuality(quality).asBufferedImage();
    }

    /**
     * 按大小,等比例缩放
     */
    public static BufferedImage zoomByRatio(InputStream inputStream, int width, int height, double quality) throws IOException {
        return Thumbnails.of(inputStream).size(width, height).outputQuality(quality).asBufferedImage();
    }

    /**
     * 按百分比,等比例缩放
     */
    public static BufferedImage zoomByRatio(String filePath, double scale, double quality) throws IOException {
        return Thumbnails.of(filePath).scale(scale).outputQuality(quality).asBufferedImage();
    }

    /**
     * 按百分比,等比例缩放
     */
    public static BufferedImage zoomByRatio(BufferedImage bufferedImage, double scale, double quality) throws IOException {
        return Thumbnails.of(bufferedImage).scale(scale).outputQuality(quality).asBufferedImage();
    }

    /**
     * 按百分比,等比例缩放
     */
    public static BufferedImage zoomByRatio(InputStream inputStream, double scale, double quality) throws IOException {
        return Thumbnails.of(inputStream).scale(scale).outputQuality(quality).asBufferedImage();
    }

    /**
     * 按大小缩放,不保持比例
     */
    public static BufferedImage zoomBySize(String filePath, int width, int height, double quality) throws IOException {
        return Thumbnails.of(filePath).size(width, height).outputQuality(quality).keepAspectRatio(false).asBufferedImage();
    }

    /**
     * 按大小缩放,不保持比例
     */
    public static BufferedImage zoomBySize(BufferedImage bufferedImage, int width, int height, double quality) throws IOException {
        return Thumbnails.of(bufferedImage).size(width, height).outputQuality(quality).keepAspectRatio(false).asBufferedImage();
    }

    /**
     * 按大小缩放,不保持比例
     */
    public static BufferedImage zoomBySize(InputStream inputStream, int width, int height, double quality) throws IOException {
        return Thumbnails.of(inputStream).size(width, height).outputQuality(quality).keepAspectRatio(false).asBufferedImage();
    }

    public static BufferedImage rotate(String filePath, int width, int height, double angle) throws IOException {
        return Thumbnails.of(filePath).size(width, height).rotate(angle).asBufferedImage();
    }

    public static BufferedImage rotate(BufferedImage bufferedImage, int width, int height, double angle) throws IOException {
        return Thumbnails.of(bufferedImage).size(width, height).rotate(angle).asBufferedImage();
    }

    public static BufferedImage rotate(InputStream inputStream, int width, int height, double angle) throws IOException {
        return Thumbnails.of(inputStream).size(width, height).rotate(angle).asBufferedImage();
    }


    public static BufferedImage watermark(String filePath, String text, int fontSize, Color color) throws IOException {
        return Thumbnails.of(filePath).scale(1f).watermark(Positions.BOTTOM_RIGHT, drawWatermarkImg(text, fontSize, color), 0.70f).asBufferedImage();
    }

    public static BufferedImage watermark(BufferedImage bufferedImage, String text, int fontSize, Color color) throws IOException {
        return Thumbnails.of(bufferedImage).scale(1f).watermark(Positions.BOTTOM_RIGHT, drawWatermarkImg(text, fontSize, color), 0.70f).asBufferedImage();
    }

    public static BufferedImage drawWatermarkImg(String text, int fontSize, Color color) {
        Graphics2D graphics = null;
        BufferedImage watermarkImage = null;
        try {
            Font font = new Font("", Font.BOLD, fontSize);
            FontMetrics fm = new JLabel(text).getFontMetrics(font);

            int extraSize = fontSize / 10;// 预留多出一点宽度和高度
            int width = fm.charsWidth(text.toCharArray(), 0, text.length()) + extraSize; // 所有设置字体的字符总宽度
            int height = fontSize + extraSize;// 高度

            logger.debug("watermark fontSize={},extraSize={}, watermarkImage:width={},height={}", fontSize, extraSize, width, height);

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

        return watermarkImage;
    }

    /**
     * @param bufferedImage
     * @param formatName
     * @param filePath
     * @return boolean 操作是否成功
     */
    public static boolean toFile(BufferedImage bufferedImage, String formatName, String filePath) {
        checkToParam(bufferedImage, formatName, filePath);
        formatName = formatName.trim();
        try {
            ImageIO.write(bufferedImage, formatName, new File(filePath));
            return true;
        } catch (IOException e) {
            logger.error("saveToPath error. filePath=[{}]", filePath, e);
        }
        return false;
    }

    /**
     * @param bufferedImage
     * @param formatName
     * @return 图片的base64 String
     */
    public static String toBase64(BufferedImage bufferedImage, String formatName) {
        checkToParam(bufferedImage, formatName);
        formatName = formatName.trim();
        String type = "data:image/" + formatName + ";base64,";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, formatName, outputStream);
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

    public static void setHeader(HttpServletResponse response, String formatName, String content) {
        response.setContentType("image/" + formatName);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("CustomContent", content);// content
    }

    public static boolean toWebResponse(HttpServletResponse response, BufferedImage bufferedImage, String formatName, String content) {
        // 检查ToX参数合法性
        checkToParam(bufferedImage, formatName);
        formatName = formatName.trim();

        // 设置头信息
        setHeader(response, formatName, content);

        // 输出到OutputStream
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(bufferedImage, formatName, out);
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

    public static boolean checkToParam(BufferedImage bufferedImage, String formatName, String... otherParam) throws IllegalArgumentException {
        if (StringUtils.isBlank(formatName) || !IMG_EXT_LIST.contains(formatName.trim())) {
            throw new IllegalArgumentException("param error:formatName=" + formatName + " is not support in " + IMG_EXT_LIST.toString());
        }
        if (bufferedImage == null) {
            throw new IllegalArgumentException("param error:bufferedImage can not be null.");
        }
        if (otherParam != null) {
            Arrays.stream(otherParam).forEach(s -> {
                if (StringUtils.isBlank(s)) {
                    throw new IllegalArgumentException("param error:param can not be null,please check.");
                }
            });
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        String filePath = "./tmp/readFileFromUrl.png";
        String url = "https://www.baidu.com/img/flexible/logo/pc/result@2.png";
        ImageUtil.getFromUrl(url, filePath);

        String srcFilePath = "./tmp/src_img.jpg";
        BufferedImage zoomImage = ImageUtil.zoomByRatio(srcFilePath, 0.2, 0.9);
        ImageUtil.toFile(zoomImage, ImageUtil.FMT_JPG, "./tmp/src_img_0.2.jpg");

        BufferedImage watermarkImage = ImageUtil.watermark(srcFilePath, "Power by demo. @demo inc.", 30, Color.lightGray);
        ImageUtil.toFile(watermarkImage, ImageUtil.FMT_JPG, "./tmp/src_img_watermark.jpg");

        System.out.println(ImageUtil.toBase64(zoomImage, ImageUtil.FMT_JPG));

        zoomImage = null;
        watermarkImage = null;
    }
}
