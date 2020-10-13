package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺评分
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberShopScoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Long goodsId;



}
