package com.jjg.member.model.enums;

/**
 * 退款方式
 * @author zjp
 * @version v7.0
 * @since v7.0 上午9:47 2018/5/3
 */
public enum RefundWayEnum {
    //原路退回
    ORIGINAL("原路退回"),
    //线下支付
    OFFLINE("线下支付");

    private String description;

    RefundWayEnum(String des){
        this.description=des;

    }

    public String description(){
        return this.description;
    }

    public String value(){
        return this.name();
    }
}
