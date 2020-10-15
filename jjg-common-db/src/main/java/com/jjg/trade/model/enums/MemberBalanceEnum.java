package com.jjg.trade.model.enums;

/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/7/31 16:28
 */
public enum MemberBalanceEnum {
    /** 加余额 */
    ADD_BALANCE("加余额"),

    /** 减余额 */
    REDUCE_BALANCE("减余额");

    private String description;

    MemberBalanceEnum(String description){
        this.description=description;

    }

    public String description(){
        return this.description;
    }

    public String value(){
        return this.name();
    }
}
