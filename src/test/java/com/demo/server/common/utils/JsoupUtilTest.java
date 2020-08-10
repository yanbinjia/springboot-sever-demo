package com.demo.server.common.utils;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

public class JsoupUtilTest {

    @Test
    public void writeToFileFromUrl() throws Exception {

        String filePathLocal = "./tmp/readFileFromUrl.png";
        String url = "https://www.baidu.com/img/flexible/logo/pc/result@2.png";

        Response response = Jsoup.connect(url).ignoreContentType(true).execute();

        // output here
        FileOutputStream out = new FileOutputStream(new File(filePathLocal));
        out.write(response.bodyAsBytes());
        // resultImageResponse.body() is where the image's contents are.
        out.close();
    }
}
