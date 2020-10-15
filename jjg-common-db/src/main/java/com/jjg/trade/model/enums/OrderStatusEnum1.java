package com.jjg.trade.model.enums;

/**
 * 申请退款 流程枚举类
 *
 * @author AJIN
 * @version 1.0
 * @since v7.0.0
 * 2019年08月10日下午2:44:54
 */
public enum OrderStatusEnum1 {

    /**
     * 已确认 待付款
     */
    CONFIRM("已确认"),

    /**
     * 已取消
     */
    CANCELLED("已取消"),

    /**
     * 已付款 待发货
     */
    PAID_OFF("已付款"),

    /**
     * 申请退款
     */
    APPLY_REFUND("申请退款"),

    /**
     * 已发货 待收货
     */
    SHIPPED("已发货"),

    /**
     * 已完成
     */
    COMPLETE("已完成");

    private String description;

    OrderStatusEnum1(String description) {
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
        for(OrderStatusEnum1 order: OrderStatusEnum1.values()){
            if(code.equals(order.value())){
                return order.getDescription();
            }
        }
        return "";
    }

}
