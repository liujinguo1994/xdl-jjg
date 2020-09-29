package com.xdl.jjg.properties;

/**
 * @Author: ciyuan
 * @Date: 2019/5/20 0:29
 * TODO: 生成图形验证码相关的默认参数
 */
public class ImageCodeProperties extends SmsCodeProperties{

    public ImageCodeProperties() {
        setLength(4);
    }

    /**
     * 长度
     */
    private int width = 67;

    /**
     * 宽度
     */
    private int height = 23;

    public ImageCodeProperties(int length, int expireIn, String url) {
        super(length, expireIn, url);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


}
