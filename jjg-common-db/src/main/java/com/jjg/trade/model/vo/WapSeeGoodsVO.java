package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-04-16 16:30:01
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class WapSeeGoodsVO implements Serializable {

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
     * 商品价格
     */
	@ApiModelProperty(value = "商品价格 SKU最小金额")
	private Double money;


    /**
     * 卖家id
     */

	@ApiModelProperty(value = "卖家id")
	private Long shopId;


    /**
     * 卖家名字
     */
	@ApiModelProperty(value = "卖家名字")
	private String shopName;


    /**
     * 购买数量
     */
	@ApiModelProperty(value = "购买数量")
	private Integer buyCount;

	/**
	 * 图片地址
	 */
	@ApiModelProperty(value = "图片地址")
	private String original;

	protected Serializable pkVal() {
		return this.id;
	}

}
