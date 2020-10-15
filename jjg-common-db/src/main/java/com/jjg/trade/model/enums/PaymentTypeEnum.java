package com.jjg.trade.model.enums;

/**
 * 支付方式
 * @author Snow create in 2018/4/8
 * @version v2.0
 * @since v7.0.0
 */
public enum PaymentTypeEnum {

    /**
     * 在线支付
     */
    ONLINE("在线支付"),

    /**
     * 货到付款
     */
    COD("货到付款");

    private String description;


    PaymentTypeEnum(String description){
        this.description = description;
    }

    public static PaymentTypeEnum defaultType(){

        return ONLINE;
    }

    public String description(){
        return this.description;
    }

    public String value(){
        return this.name();
    }

}
