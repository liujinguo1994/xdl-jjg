package com.jjg.member.model.dto;

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
public class EsParameterGroupDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 参数组名称
     */
	private String groupName;
    /**
     * 关联分类id
     */
	private Long categoryId;
    /**
     * 排序
     */
	private Integer sort;

	protected Serializable pkVal() {
		return this.id;
	}

}
