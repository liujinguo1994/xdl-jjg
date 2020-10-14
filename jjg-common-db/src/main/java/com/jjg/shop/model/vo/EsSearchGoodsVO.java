package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSearchGoodsVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 商品编号
     */
	@ApiModelProperty(value = "商品编号")
	private String goodsSn;
    /**
     * 品牌id
     */

	@ApiModelProperty(value = "品牌id")
	private Long brandId;

    /**
     * 分类id
     */
	@ApiModelProperty(value = "分类id")
	private Long categoryId;


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

	/**
	 * 卖家id
	 */
	@ApiModelProperty(value = "卖家id")
	private Long shopId;
	/**
	 * 购买数量
	 */
	@ApiModelProperty(value = "购买数量")
	private Integer buyCount;

}
