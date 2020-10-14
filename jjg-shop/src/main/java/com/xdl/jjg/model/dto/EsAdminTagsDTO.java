package com.jjg.member.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-07-27 14:57:57
 */
@Data
@Accessors(chain = true)
public class EsAdminTagsDTO implements Serializable {

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
