package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsPromotionGoodsQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品id")
	private Long goodsId;

	/**
	 * SkuId
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "SkuId")
	private Long skuId;

    /**
     * 活动开始时间
     */
	@ApiModelProperty(value = "活动开始时间")
	private Long createTime;

    /**
     * 活动结束时间
     */
	@ApiModelProperty(value = "活动结束时间")
	private Long updateTime;

    /**
     * 活动id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "活动id")
	private Long activityId;

    /**
     * 活动类型
     */
	@ApiModelProperty(value = "活动类型")
	private String promotionType;

    /**
     * 活动标题
     */
	@ApiModelProperty(value = "活动标题")
	private String title;

    /**
     * 参与活动的商品数量
     */
	@ApiModelProperty(value = "参与活动的商品数量")
	private Integer num;

    /**
     * 活动时商品的价格
     */
	@ApiModelProperty(value = "活动时商品的价格")
	private Double price;

    /**
     * 商家ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商家ID")
	private Long shopId;

}
