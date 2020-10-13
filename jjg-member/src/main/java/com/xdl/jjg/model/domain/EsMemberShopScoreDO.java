package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺评分
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberShopScoreDO implements Serializable {


    /**
     * 主键
     */
	private Long id;
    /**
     * 会员id
     */
	private Long memberId;
    /**
     * 订单编号
     */
	private String orderSn;
    /**
     * 发货速度评分
     */
	private Double deliveryScore;
    /**
     * 描述相符度评分
     */
	private Double descriptionScore;
    /**
     * 服务评分
     */
	private Double serviceScore;
    /**
     * 卖家id
     */
	private Long shopId;

    /**
     * 商品id
     */
    private Long goodId;

}
