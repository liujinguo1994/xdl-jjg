package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 自提信息表VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-06 14:34:35
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsDeliveryInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 订单编号
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 订单明细编号
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "订单明细编号")
	private Long orderDetailSn;

    /**
     * 自提内容
     */
	@ApiModelProperty(value = "自提内容")
	private String content;

    /**
     * 是否完成
     */
	@ApiModelProperty(value = "是否完成")
	private Integer isOk;

    /**
     * 商品SKU_ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品SKU_ID")
	private Long skuId;

	/**
	 * 商品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;

	protected Serializable pkVal() {
		return this.id;
	}

}
