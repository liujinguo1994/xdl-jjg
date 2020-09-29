package com.xdl.jjg.test;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.UUID;

public class FileTest {

    public static void main(String[] args) {

        try {
            String pic = "https://jjgoss.xindongle.com/images/2019/9/30/2569241658204218b6bcc6e089034162.jpg";
//            String pic = "https://jjgxdl.yunzaijia.com/jjg-test/7f1588b0-cae5-11e9-b3e6-9b5836991d68.jpeg";

            String fileName = UUID.randomUUID().toString()
                    .replaceAll("-", "") + pic.substring(pic.lastIndexOf("."));
            URL url = new URL(pic);
            BufferedImage image = ImageIO.read(url);
            File outputfile  = new File("/save.png");
            ImageIO.write(image,"png",outputfile);
//            image.get
            System.out.println("Width"+image.getWidth()+"****Height:"+image.getHeight());
            File newFile = new File("/" + fileName);
            //outputQuality用来控制图片质量的
            Thumbnails.of(url).size(375,375).toFile(newFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
