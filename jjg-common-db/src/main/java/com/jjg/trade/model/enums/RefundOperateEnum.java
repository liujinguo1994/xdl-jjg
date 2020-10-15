package com.jjg.trade.model.enums;

/**
 * 售后操作枚举类
 * @author zjp
 * @version v7.0
 * @since v7.0 下午4:54 2018/5/2
 */
public enum RefundOperateEnum {

    /**
     * 申请售后
     */
    APPLY("申请退(款)货"),

    /**
     * 卖家审核
     */
    SELLER_APPROVAL("卖家审核"),

    /**
     * 退货入库
     */
    STOCK_IN("退货入库"),

    /**
     * 卖家发货
     */
    SELLER_DELIVERY("卖家发货"),

    /**
     * 取消
     */
    CANCEL("取消"),

    /**
     * 卖家退款
     */
    SELLER_REFUND("卖家退款");

    private String description;

    RefundOperateEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
