package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 排行商品
 * </p>
 *
 * @author rm 2817512105@qq.com
 */
@Data
@ApiModel
public class EsSalesRankingGoodsVO implements  Serializable {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 商品名称
     */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;
	/**
	 * 购买数量
	 */
	@ApiModelProperty(value = "购买数量")
	private Integer buyCount;
	/**
	 * 原图路径
	 */
	@ApiModelProperty(value = "原图路径")
	private String original;
    /**
     * 商品价格
     */
	@ApiModelProperty(value = "商品价格")
	private Double money;
}
