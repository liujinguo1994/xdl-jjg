package com.xdl.jjg.entity;

import lombok.Data;

/**
 * @ClassName: EsExportOrder
 * @Description: 导出Excel实体类
 * @Author: bamboo  asp.bamboo@gmail.com
 * @Date: 2019/9/2 16:10
 * @Version: 1.0
 */
@Data
public class EsExportOrder {

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 退款单编号
     */
    private String refundSn;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品价格
     */
    private Double goodsMoney;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 订单金额（退款金额）
     */
    private Double money;

    /**
     * 优惠金额
     */
    private Double discountMoney;
}
