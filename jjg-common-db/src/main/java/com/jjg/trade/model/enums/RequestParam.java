package com.jjg.trade.model.enums;

/**
 * @ClassName: RequestParam
 * @Description: 支付要用到的请求参数
 * @Author: libw  981087977@qq.com
 * @Date: 7/10/2019 14:50
 * @Version: 1.0
 */
public enum RequestParam {

    /**
     * 请求域名, 用于PC支付
     */
    DOMAIN("domain"),

    /**
     * IP地址
     */
    IP("ip");

    private String name;

    RequestParam (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
