package com.jjg.trade.model.enums;

/**
 * @ClassName: PayConfigParam
 * @Description: 支付配置参数名称
 * @Author: libw  981087977@qq.com
 * @Date: 7/10/2019 15:38
 * @Version: 1.0
 */
public enum PayConfigParam {

    /**
     * 开启状态
     */
    IS_OPEN("isOpen"),

    /**
     * 配置列表
     */
    CONFIG_LIST("configList");

    private String name;

    PayConfigParam(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
