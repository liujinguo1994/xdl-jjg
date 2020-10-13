package com.xdl.jjg.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品折扣活动表Form
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-28 14:26:05
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGoodsDiscountForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动开始时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "活动开始时间")
	private Long startTime;

    /**
     * 活动结束时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "活动结束时间")
	private Long endTime;

    /**
     * 折扣
     */
	@ApiModelProperty(value = "折扣")
	private Double discount;

    /**
     * 活动标题
     */
	@ApiModelProperty(value = "活动标题")
	private String title;

    /**
     * 商品参与方式（0，全部商品 1，部分商品）
     */
	@ApiModelProperty(value = "商品参与方式（1，全部商品 2，部分商品）")
	private Integer rangeType;


    /**
     * 描述
     */
	@ApiModelProperty(value = "描述")
	private String remark;

	@ApiModelProperty(value = "参与活动商品集合")
	private List<EsPromotionGoodsForm> goodsList;

}
