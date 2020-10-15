package com.jjg.member.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 提供给买家端 的评价信息列表
 * 我的订单 订单商品详情
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsComplaintBuyerOrderItemsDO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 商品ID
	 */
	private Long goodsId;
	/**
	 * skuID
	 */
	private Long skuId;
	/**
	 * 数量
	 */
	private Integer num;
	/**
	 * 图片
	 */
	private String image;
	/**
	 * 商品名称
	 */
	private String name;
	/**
	 * 优惠后价格
	 */
	private Double money;
}
