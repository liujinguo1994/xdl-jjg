package com.xdl.jjg.code.image;


import com.xdl.jjg.code.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @Author: ciyuan
 * @Date: 2019/5/19 17:49
 * TODO 图片验证码类
 */
public class ImageCode extends ValidateCode {

    private static final long serialVersionUID = -528924340428400276L;

    /**
     * 展示给用户的图片
     */
    private BufferedImage image;

    /**
     * 该构造器用于设置过期时间的秒数
     * @param image
     * @param code
     * @param expireIn 过期时间的秒数   该验证码在expireIn秒后过期
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }

    /**
     * 该构造器用于设置过去时间的具体时间
     * @param image
     * @param code
     * @param expireTime 验证码过期的具体时间 该验证码在expireTime过期
     */
    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code, expireTime);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
