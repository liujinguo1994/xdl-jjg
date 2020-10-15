package com.jjg.trade.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 原路退回使用vo
 * @Author: libw  981087977@qq.com
 * @Date: 6/11/2019 15:37
 * @Version: 1.0
 */
@Data
public class RefundBillVO implements Serializable {

    private static final long serialVersionUID = 6902367702212390171L;

    /**
     * 退款编号
     */
    private String refundSn;

    /**
     * 退款金额
     */
    private Double refundPrice;

    /**
     * 交易金额
     */
    private Double tradePrice;

    /**
     * 第三方订单号
     */
    private String returnTradeNo;

    /**
     * 支付时使用的参数
     */
    private Map<String,String> configMap;
}
