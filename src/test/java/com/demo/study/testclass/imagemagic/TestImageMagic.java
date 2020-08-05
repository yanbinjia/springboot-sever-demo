/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-04T18:28:12.828+08:00
 */

package com.demo.study.testclass.imagemagic;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;

import java.io.IOException;

public class TestImageMagic {
    public static void main(String[] args) throws InterruptedException, IOException, IM4JavaException {
        /**
         * https://imagemagick.org/script/command-line-processing.php
         *
         */
        IMOperation opRotate = new IMOperation();
        opRotate.addImage("./tmp/qrcode1.png");
        opRotate.rotate(90D);
        opRotate.addImage("./tmp/qrcode1_1.png");
        ImageCommand convert = new ConvertCmd();
        convert.run(opRotate);
        System.out.println("旋转图片成功");

        IMOperation opResize = new IMOperation();
        opResize.addImage("./tmp/qrcode2.png");
        opResize.resize(1000);//等比例缩放,高度和宽度需符合图片比例,不符合的情况下,以其中最小值为准
        opResize.addImage("./tmp/qrcode2_1.png");
        ImageCommand convertResize = new ConvertCmd();
        convertResize.run(opResize);
        System.out.println("缩放图片成功");

        IMOperation opWatermark = new IMOperation();
        opWatermark.addImage("./tmp/qrcode3.png");
        // southeast 右下角
        opWatermark.font("ArialBold").gravity("southeast").pointsize(40).fill("#F2F2F2").draw("text 10,10 " + "demo");
        opWatermark.addImage("./tmp/qrcode3_1.png");
        ImageCommand convertWatermark = new ConvertCmd();
        convertWatermark.run(opWatermark);
        System.out.println("添加文字水印成功");

        IMOperation opop = new IMOperation();
        opop.addImage("./tmp/qrcode3.png");
        opop.thumbnail(200);
        opop.addImage();
        ImageCommand convertThumbnail = new ConvertCmd();
        convertThumbnail.run(opop);
        System.out.println("缩略图成功");

    }
}
