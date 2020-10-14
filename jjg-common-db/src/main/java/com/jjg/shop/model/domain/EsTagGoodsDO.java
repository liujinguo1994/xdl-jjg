package com.xdl.jjg.model.domain;

import lombok.Data;

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
public class EsTagGoodsDO implements Serializable {

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
