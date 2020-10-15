package com.jjg.trade.model.enums;

/**
 * 订单状态
 *
 * @author AJIN
 * @version 1.0
 * @since v7.0.0
 * 2019年08月10日下午2:44:54
 */
public enum OrderStatusEnum {

    /**
     * 新订单
     */
    NEW("新订单"),

    /**
     * 出库失败
     */
    INTO_DB_ERROR("出库失败"),

    /**
     * 已确认 待付款
     */
    CONFIRM("已确认"),

    /**
     * 已付款 待发货
     */
    PAID_OFF("已付款"),

    /**
     * 已发货 待收货
     */
    SHIPPED("已发货"),

    /**
     * 已收货 待评价
     */
    ROG("已收货"),

    /**
     * 已完成
     */
    COMPLETE("已完成"),

    /**
     * 已取消
     */
    CANCELLED("已取消"),

    /**
     * 售后中
     */
    AFTER_SERVICE("售后中"),

    /**
     * 申请退款
     */
    APPLY_REFUND("申请退款"),

    /**
     * 退款完成
     */
    APPLY_REFUND_COMPLETE("退款完成"),

    /**
     * 已售后
     */
    AFTER_APPLY_SERVICE("已售后");


    private String description;

    OrderStatusEnum(String description) {
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

    public static String getOrderName(String code){
        for(OrderStatusEnum order:OrderStatusEnum.values()){
            if(code.equals(order.value())){
                return order.getDescription();
            }
        }
        return "";
    }

}
