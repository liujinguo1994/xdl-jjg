package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
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
public class EsComplaintBuyerOrderItemsVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(required = false, value = "主键id")
	private Long id;
	/**
	 * 商品ID
	 */
	@ApiModelProperty(required = false, value = "主键id")
	private Long goodsId;
	/**
	 * skuID
	 */
	@ApiModelProperty(required = false, value = "skuID")
	private Long skuId;
	/**
	 * 数量
	 */
	@ApiModelProperty(required = false, value = "数量")
	private Integer num;
	/**
	 * 图片
	 */
	@ApiModelProperty(required = false, value = "图片")
	private String image;
	/**
	 * 商品名称
	 */
	@ApiModelProperty(required = false, value = "商品名称")
	private String name;
	/**
	 * 优惠后价格
	 */
	@ApiModelProperty(required = false, value = "价格")
	private Double money;
}
