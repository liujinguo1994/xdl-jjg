package com.jjg.member.model.enums;

/**
 * 售后状态枚举类
 * @author zjp
 * @version v7.0
 * @since v7.0 上午9:25 2018/5/3
 */
public enum RefundStatusEnum {

    /**
     * 申请中
     */
    APPLY("申请中"),

    /**
     * 申请通过
     */
    PASS("申请通过"),

    /**
     * 审核拒绝
     */
    REFUSE("审核拒绝"),

    /**
     * 申请取消
     */
    CANCEL("申请取消");

    private String description;

    RefundStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
    return this.description;
}

    public String value() {
        return this.name();
    }
}
