package com.jjg.trade.model.enums;

/**
 * 交易类型
 *
 * @author 李博文
 * @Author: libw  981087977@qq.com
 * @Date: 6/3/2019 19:53
 * @Version: 1.0
 */
public enum TradeType {

    /**
     * 订单类型
     */
    order("订单"),
    /**
     * 交易类型
     */
    trade("交易");

    private String description;

    TradeType(String description) {
        this.description = description;
    }
}
