package com.xdl.jjg.properties;

/**
 * @Author: ciyuan
 * @Date: 2019/5/20 0:32
 * TODO: 验证码相关的参数的注入，包括图形验证码和短信验证码
 */

public class ValidateCodeProperties {

    private ImageCodeProperties image = new ImageCodeProperties();

    private SmsCodeProperties sms = new ImageCodeProperties();

    private SecretCodeProperties secret = new SecretCodeProperties();

    public SecretCodeProperties getSecret() {
        return secret;
    }

    public void setSecret(SecretCodeProperties secret) {
        this.secret = secret;
    }

    public SmsCodeProperties getSms() {
        return sms;
    }

    public void setSms(SmsCodeProperties sms) {
        this.sms = sms;
    }

    public ImageCodeProperties getImage() {
        return image;
    }

    public void setImage(ImageCodeProperties image) {
        this.image = image;
    }
}
