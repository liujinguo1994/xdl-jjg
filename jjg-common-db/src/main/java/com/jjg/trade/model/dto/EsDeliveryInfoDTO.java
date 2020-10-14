package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 自提信息表
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-06 14:34:35
 */
@Data
@Accessors(chain = true)
public class EsDeliveryInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 订单编号
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private String orderSn;

    /**
     * 订单明细编号
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long orderDetailSn;

    /**
     * 自提内容
     */
	private String content;

    /**
     * 是否完成
     */
	private Integer isOk;

    /**
     * 商品SKU_ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 商品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long goodsId;

	protected Serializable pkVal() {
		return this.id;
	}

}
