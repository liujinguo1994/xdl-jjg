package com.jjg.member.model.enums;
/**
 * <p>
 * 消费类型
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-07-18
 */
public enum ConsumeEnumType {

    /**
     * 消费
     */
    CONSUME("消费"),
    /**
     * 充值
     */
    RECHARGE("充值"),
    /**
     * 退款
     */
    REFUND("退款");

    private String description;

    ConsumeEnumType(String description) {
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
}
