package com.xdl.jjg.model.enums;

/**
 * @Description: 文章显示位置
 */
public enum ArticleShowPosition {

    /**
     * 注册协议
     */
    REGISTRATION_AGREEMENT("注册协议"),
    /**
     * 入驻协议
     */
    COOPERATION_AGREEMENT("入驻协议"),
    /**
     * 平台联系方式
     */
    CONTACT_INFORMATION("平台联系方式"),

    /**
     * 团购活动协议
     */
    GROUP_BUY_AGREEMENT("团购活动协议"),


    /**
     * 其他
     */
    OTHER("其他");

    private String description;

    ArticleShowPosition(String description) {
        this.description = description;

    }
    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

}
