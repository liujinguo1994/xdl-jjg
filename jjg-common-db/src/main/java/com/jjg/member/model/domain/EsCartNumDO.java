package com.jjg.member.model.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-03
 */
@Data
@ToString
public class EsCartNumDO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 购物车ID
	 */
	private Long cartId;


	/**
	 * sku数量
	 */
	private Integer skuNum;
}
