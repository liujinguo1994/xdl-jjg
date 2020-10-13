package com.xdl.jjg.model.enums;

/**
 *
 * 库存日志类型
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/6/5
 */
public enum QuantityLogType {

    /**
     * 订单创建扣减库存
     */
    ORDER_CREATE,

    /**
     * 订单取消
     */
    ORDER_CANCEL,

    /**
     * 库存回滚
     */
    ROLLBACK,

    /**
     * 发货
     */
    SHIP,

    /**
     * 退货
     */
    RETURN;
}