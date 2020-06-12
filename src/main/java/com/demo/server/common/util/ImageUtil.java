package com.demo.server.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtil {

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
			log.error("isImage error", e);
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

		try (FileOutputStream out = new FileOutputStream(new File(filePath));) {
			Response response = Jsoup.connect(url).ignoreContentType(true).execute();
			out.write(response.bodyAsBytes());
			out.flush();
			result = true;
		} catch (IOException e) {
			log.error("getFileFromUrl error. url={},path={}", url, filePath, e);
		}

		return result;
	}

	public static void main(String[] args) {
		System.out.println(ImageUtil.isImageByExt("/sf/sf/saf/ddd.txt.png"));

		String filePath = "./tmp/readFileFromUrl.png";
		String url = "https://www.baidu.com/img/flexible/logo/pc/result@2.png";

		ImageUtil.getFromUrl(url, filePath);

	}
}
