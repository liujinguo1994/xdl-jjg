package com.jjg.member.model.enums;

/**
 * 申请售后类型枚举类
 * @author zjp
 * @version v7.0
 * @since v7.0 上午9:38 2018/5/3
 */
public enum RefundTypeEnum {

    /**
     * 取消订单
     */
    CANCEL_ORDER("取消订单"),

    /**
     *申请售后
     */
    AFTER_SALE("申请售后"),

    /**
     * 退款 (已付款 但是买家未发货)
     */
    RETURN_MONEY("退款"),

    /**
     * 退货 (就是退货 退款)
     */
    RETURN_GOODS("退货"),

    /**
     * 换货
     */
    CHANGE_GOODS("换货"),

    /**
     * 维修
     */
    REPAIR_GOODS("维修");

    private String description;

    RefundTypeEnum(String des){
        this.description=des;
    }

    public String description(){
        return this.description;
    }

    public String value(){
        return this.name();
    }
}
