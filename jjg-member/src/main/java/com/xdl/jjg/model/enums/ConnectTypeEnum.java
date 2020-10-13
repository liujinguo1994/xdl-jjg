package com.xdl.jjg.model.enums;

/**
 * <p>
 * 联合登录类型
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-18
 */
public enum ConnectTypeEnum {
    //QQ联合登录
    QQ("QQ"),
    //微信联合登录
    WECHAT("微信联合登录"),
    //支付宝登录
    ALIPAY("支付宝登录");

    private String description;

    ConnectTypeEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

}
