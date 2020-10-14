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
public class EsDraftGoodsParamsDO implements  Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 草稿ID
     */
	private Long draftGoodsId;
    /**
     * 参数ID
     */
	private Long paramId;
    /**
     * 参数名
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
