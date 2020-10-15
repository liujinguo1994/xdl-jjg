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
 * 自提信息表QueryForm
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-06 14:34:36
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsDeliveryInfoQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "订单编号")
	private Long orderSn;

    /**
     * 订单明细编号
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "订单明细编号")
	private Long orderDetailSn;

    /**
     * 商品SKU_ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品SKU_ID")
	private Long skuId;

}
