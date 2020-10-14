package com.jjg.member.model.enums;

/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/7/18 16:07
 */
public enum StockTypeEnum {

    /**
     * 取消订单，增加库存
     */
    INCREASE("增加库存"),
    /**
     * 生成订单，减少库存
     */
    REDUCE("减库存");

    private String description;

    StockTypeEnum(String description){
        this.description = description;
    }

    public String description(){
        return this.description;
    }
    public String value() {
        return this.name();
    }
}
