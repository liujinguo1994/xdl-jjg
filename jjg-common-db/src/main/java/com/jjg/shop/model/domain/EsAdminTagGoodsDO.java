package com.jjg.shop.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-07-27 14:57:56
 */
@Data

public class EsAdminTagGoodsDO implements  Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
	private Long tagId;

    /**
     * 商品id
     */
	private Long goodsId;

}
