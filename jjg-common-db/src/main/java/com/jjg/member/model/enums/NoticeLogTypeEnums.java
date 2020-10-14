package com.jjg.member.model.enums;

/**
 * <p>
 * 积分类型枚举类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
public enum NoticeLogTypeEnums {
    /**
     * 订单
     */
    ORDER("订单"),
    /**
     * 商品
     */
    GOODS("商品"),
    /**
     * 售后
     */
    AFTERSALE("售后");
    private String description;

    NoticeLogTypeEnums(String description) {
        this.description = description;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

}
