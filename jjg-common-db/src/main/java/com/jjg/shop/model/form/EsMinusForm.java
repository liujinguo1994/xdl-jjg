package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Api
public class EsMinusForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单品立减金额
     */
    @ApiModelProperty(value = "单品立减金额")
	private Double singleReductionValue;
	/**
	 * 活动开始时间
	 */
	@ApiModelProperty(value = "活动开始时间")
	private Long startTime;
	/**
	 * 活动结束时间
	 */
	@ApiModelProperty(value = "活动结束时间")
	private Long endTime;
    /**
     * 单品立减活动标题
     */
	@ApiModelProperty(value = "单品立减活动标题")
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
