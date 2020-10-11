package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单设置
 */
@Data
public class EsOrderSettingDTO implements Serializable {

    private static final long serialVersionUID = -1549672177047538309L;
    /**
     * 显示几天内订单天数
     */
    private Integer showOrderDay;
    /**
     * 待支付订单自动关闭时间
     */
    private Integer closeOrderDay;
    /**
     * 发货几天自动完成订单
     */
    private Integer finishOrderDay;
    /**
     * 完成订单几天内可以退货
     */
    private Integer returnedGoodsDay;
    /**
     * 完成订单几天内可以换货
     */
    private Integer exchangeGoodsDay;
    /**
     * 订单发票税率
     */
    private Double invoiceTaxRate;
    /**
     * 自动确认收货天数
     */
    private Integer autoReceivingDay;


}
