package com.jjg.trade.model.enums;

/**
 * @ClassName: ProcessStatus
 * @Description: 处理状态枚举
 * @Author: libw  981087977@qq.com
 * @Date: 7/4/2019 09:49
 * @Version: 1.0
 */
public enum ProcessStatusEnum {

    /**
     *待处理
     */
    TO_BE_PROCESS("待处理"),

    /**
     * 待退款
     */
    WAIT_REFUND("待退款"),

    /**
     * 退款失败
     */
    REFUND_FAIL("退款失败"),

    /**
     * 待入库
     */
    WAIT_IN_STORAGE("待入库"),

    /**
     * 待发货
     */
    WAIT_SHIP("待发货"),

    /**
     * 完成
     */
    COMPLETED("完成");


    private String description;

    ProcessStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
