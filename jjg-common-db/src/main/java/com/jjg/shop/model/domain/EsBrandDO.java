package com.jjg.member.model.domain;

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
public class EsBrandDO implements  Serializable {

    private static final long serialVersionUID = 1L;
	private Long id;
    /**
     * 品牌名称
     */
	private String name;
    /**
     * 品牌图标
     */
	private String logo;
    /**
     * 是否删除，0删除1未删除
     */
	private Integer isDel;
	protected Serializable pkVal() {
		return this.id;
	}
}
