package com.jjg.trade.model.enums;

/**
 * 订单来源枚举
 *
 * @author yuanj create in 2018/8/3
 * @version v2.0
 * @since v7.0.0
 */
public enum OrderResourceEnum {


    /** 卓付商城 */
    ZHUOFU("卓付商城"),

    /** 联华 */
    LIANHUA("联华"),

    /** 中国人寿 */
    LFC("中国人寿"),
    /** XXX */
    XXX("XXX");

    private String description;


    OrderResourceEnum(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String value(){
        return this.name();
    }



}
