package com.jjg.trade.model.enums;

/**
 * 支付模式枚举
 *
 * @author zh create in 2018/4/8
 * @version v2.0
 * @since v7.0.0
 */
public enum PayMode {

    /**
     * 正常
     */
    normal("正常"),

    /**
     * 二维码
     */
    qr("二维码");

    private String description;


    PayMode(String description) {
        this.description = description;
    }


    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

}
