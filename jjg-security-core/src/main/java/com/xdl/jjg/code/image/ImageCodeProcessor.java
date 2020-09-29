package com.xdl.jjg.code.image;

import com.xdl.jjg.code.impl.AbstractValidateCodeProcessor;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: ciyuan
 * @Date: 2019/5/21 22:39
 */
@Component("imageValidateCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    private static Logger logger = LoggerFactory.getLogger(ImageCodeProcessor.class);
    /**
     * 发送图形验证码，将其写到响应中
     * 直接用文件流来处理
     * BASE64格式
     * @param request
     * @param imageCode
     * @throws Exception
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
        logger.info("----------------图片验证码：" + imageCode.getCode());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imageCode.getImage(), "JPEG", baos);
        byte[] bytes = baos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String jpeg_base64 =  encoder.encodeBuffer(bytes).trim();
        jpeg_base64 = jpeg_base64.replaceAll("\n", "").replaceAll("\r", "");
        String newStr = new String("\"data:image/jpg;base64," + jpeg_base64 + "\"");
        request.getResponse().getWriter().write(newStr);
        //ImageIO.write(imageCode.getImage(), "JPEG", request.getResponse().getOutputStream());
    }
}
