package com.jjg.shop.model.constant;

/**
 * 商品操作类型枚举
 */
public enum  GoodsOperateType {

    INSERT("新增"),

    DELETE("删除"),

    UPDATE_BUY_COUNT("修改销量"),

    UPDATE("修改");
    private String description;

    GoodsOperateType(String des) {
        this.description = des;
    }
    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

}
