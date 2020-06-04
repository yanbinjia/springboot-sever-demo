package com.demo.server.common.util;

import java.io.File;
import java.io.FileOutputStream;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsoupUtilTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void writeToFileFromUrl() throws Exception {

		String filePathLocal = "./tmp/readFileFromUrl.png";
		String url = "https://www.baidu.com/img/flexible/logo/pc/result@2.png";

		Response responseUrl = Jsoup.connect(url).ignoreContentType(true).execute();

		// output here
		FileOutputStream out = new FileOutputStream(new File(filePathLocal));
		out.write(responseUrl.bodyAsBytes());
		// resultImageResponse.body() is where the image's contents are.
		out.close();
	}
}
