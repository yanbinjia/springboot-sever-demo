package com.demo.server.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
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

    public static BufferedImage zoomByRatio(String filePath, int width, int height, double quality) {
        if (StringUtils.isBlank(filePath)) {
            logger.error("filePath can no be blank.");
            return null;
        }
        try {
            return Thumbnails.of(filePath).size(width, height).outputQuality(quality).asBufferedImage();
        } catch (IOException e) {
            logger.error("zoomByRatio", e);
        }
        return null;
    }

    public static BufferedImage zoomByRatio(String filePath, double scale, double quality) {
        if (StringUtils.isBlank(filePath)) {
            logger.error("filePath can no be blank.");
            return null;
        }
        try {
            return Thumbnails.of(filePath).scale(scale).outputQuality(quality).asBufferedImage();
        } catch (IOException e) {
            logger.error("zoomByRatio", e);
        }
        return null;
    }

    public static BufferedImage zoomBySize(String filePath, int width, int height, double quality) {
        if (StringUtils.isBlank(filePath)) {
            logger.error("filePath can no be blank.");
            return null;
        }
        try {
            return Thumbnails.of(filePath).size(width, height).outputQuality(quality).keepAspectRatio(false).asBufferedImage();
        } catch (IOException e) {
            logger.error("zoomByRatio", e);
        }
        return null;
    }

    public static BufferedImage rotate(String filePath, int width, int height, double angle) {
        if (StringUtils.isBlank(filePath)) {
            logger.error("filePath can no be blank.");
            return null;
        }
        try {
            return Thumbnails.of(filePath).size(width, height).rotate(angle).asBufferedImage();
        } catch (IOException e) {
            logger.error("zoomByRatio", e);
        }
        return null;
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

    public static void main(String[] args) {
        System.out.println(ImageUtil.isImageByExt("/sf/sf/saf/ddd.txt.png"));
        String filePath = "./tmp/readFileFromUrl.png";
        String url = "https://www.baidu.com/img/flexible/logo/pc/result@2.png";
        ImageUtil.getFromUrl(url, filePath);

        String srcFilePath = "./tmp/src_img.jpg";
        BufferedImage bufferedImage = ImageUtil.zoomByRatio(srcFilePath, 0.2, 0.9);
        ImageUtil.saveToFile(bufferedImage, "jpg", "./tmp/src_img_0.2.jpg");
    }
}
