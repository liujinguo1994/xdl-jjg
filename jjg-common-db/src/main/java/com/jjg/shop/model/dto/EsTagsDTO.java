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
public class EsTagsDTO implements Serializable  {

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
     * 所属卖家
     */
	private Long shopId;
    /**
     * 关键字
     */
	private String mark;

	private Integer sort;
	protected Serializable pkVal() {
		return this.id;
	}

}
