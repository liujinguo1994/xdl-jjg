package com.xdl.jjg.model.cache;

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
public class EsCommercelItemsCO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车ID
     */
	private Long cartId;

    /**
     * 商品类型（1，普通商品，2赠品）
     */
	private Integer type;

    /**
     * 商品ID
     */
	private Long productId;

    /**
     * 商品SKUID
     */
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
	private Long shopId;


}
