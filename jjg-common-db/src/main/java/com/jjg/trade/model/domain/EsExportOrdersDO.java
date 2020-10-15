package com.jjg.trade.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 卖家端订单导出
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-27
 */
@Data
public class EsExportOrdersDO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;

    //订单号
    private String orderSn;

    //国寿单号
    private String lfcId;

    //会员id
    private Long memberId;

    //会员
    private String memberName;

    //skuId
    private Long skuId;

    //商品Id
    private Long goodsId;

    //商品名称
    private String name;

    //规格json
    private String specJson;

    //成交价(优惠后价格)
    private Double money;

    //购买数量
    private Integer num;

    //下单时间
    private Long createTime;

    //收货人
    private String shipName;

    //收货人电话
    private String shipMobile;

    //省
    private String shipProvince;

    //市
    private String shipCity;

    //区
    private String shipCounty;

    //镇
    private String shipTown;

    //详细地址
    private String shipAddr;

    //备注
    private String remark;

   //物流公司
    private String logiName;

   //物流单号
    private String shipNo;

    //订单状态
    private String orderState;

    /**
     * 订单状态 中文
     */
    private String orderStateText;
}


