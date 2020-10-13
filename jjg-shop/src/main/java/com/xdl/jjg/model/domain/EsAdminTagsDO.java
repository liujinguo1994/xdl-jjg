package com.xdl.jjg.model.domain;

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
public class EsAdminTagsDO implements  Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 标签名字
     */
	private String tagName;


    /**
     * 关键字
     */
	private String mark;

	protected Serializable pkVal() {
		return this.id;
	}
}
