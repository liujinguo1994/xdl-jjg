package com.jjg.shop.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

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
public class EsWarnStockDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品分类ID
     */
	private Long categoryId;
    /**
     * 预警库存
     */
	private Integer warnQuantity;
    /**
     * 店铺ID
     */
	private Long shopId;
    /**
     * 创建时间
     */
	private Long createTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
