package com.jjg.trade.model.enums;

/**
 * @ClassName: OpenStatus
 * @Description: 插件开启状态
 * @Author: libw  981087977@qq.com
 * @Date: 7/10/2019 15:33
 * @Version: 1.0
 */
public enum OpenStatus {

    /**
     * 开启
     */
    IS_OPEN("1"),

    /**
     * 没有开启
     */
    NOT_OPEN("0");

    private String code;
    OpenStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
