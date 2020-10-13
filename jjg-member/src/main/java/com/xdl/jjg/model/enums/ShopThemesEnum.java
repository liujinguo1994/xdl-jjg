package com.xdl.jjg.model.enums;

/**
 * <p>
 * 积分类型枚举类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
public enum ShopThemesEnum {
    /**
     * PC模版
     */
    PC("PC模版"),
    /**
     * WAP模版
     */
    WAP("WAP模版");
    private String description;

    ShopThemesEnum(String description) {
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
