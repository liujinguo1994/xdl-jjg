package com.jjg.member.model.enums;

/**
 * 订单元Key
 *
 * @author Snow create in 2018/7/5
 * @version v2.0
 * @since v7.0.0
 */
public enum OrderMetaKeyEnum {

    /** 新订单 */
    POINT("使用的积分"),

    /** 新订单 */
    GIFT_POINT("赠送的积分"),

    /** 新订单 */
    COUPON("赠送的优惠券"),

    /** 新订单 */
    GIFT("赠品")

    ;

    private String description;

    OrderMetaKeyEnum(String description){
        this.description=description;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String description(){
        return this.description;
    }

    public String value(){
        return this.name();
    }

}
