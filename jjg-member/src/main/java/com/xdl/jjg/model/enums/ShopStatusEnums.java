package com.xdl.jjg.model.enums;

/**
 * <p>
 * 店铺状态枚举
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-6-24
 */
public enum ShopStatusEnums {

    /**
     * 开启中
     */
    OPEN("开启中"),
    /**
     * 店铺关闭
     */
    CLOSED("店铺关闭"),
    /**
     * 申请开店
     */
    APPLY("申请开店"),
    /**
     * 审核拒绝
     */
    REFUSED("审核拒绝"),
    /**
     * 申请中
     */
    APPLYING("申请中");

    private String description;
    ShopStatusEnums(String description) {
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
