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
public class EsParameterGroupDO implements  Serializable {

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
