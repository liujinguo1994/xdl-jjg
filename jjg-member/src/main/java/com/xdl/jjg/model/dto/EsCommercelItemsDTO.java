package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 购物车项
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsCommercelItemsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 购物车ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long cartId;

    /**
     * 商品类型（1，普通商品，2赠品）
     */
	private Integer type;

    /**
     * 商品ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long productId;

    /**
     * 商品SKUID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

    /**
     * 商品价格
     */
	private Double price;

    /**
     * 商品数量
     */
	private Integer quantity;

    /**
     * 商品编号
     */
	private String goodsSn;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 修改时间
     */
	private Long updateTime;

    /**
     * 店铺ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

	protected Serializable pkVal() {
		return this.id;
	}

}
