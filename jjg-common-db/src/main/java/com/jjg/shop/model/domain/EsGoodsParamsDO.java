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
public class EsGoodsParamsDO implements  Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品id
     */
	private Long goodsId;
    /**
     * 参数id
     */
	private Long paramId;
    /**
     * 参数名字
     */
	private String paramName;
    /**
     * 参数值
     */
	private String paramValue;
	protected Serializable pkVal() {
		return this.id;
	}

}
