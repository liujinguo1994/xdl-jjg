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
public class EsSpecificationDO implements  Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 规格项名称
     */
	private String specName;
    /**
     * 是否被删除0未 删除   1  删除
     */
	private Integer isDel;
    /**
     * 规格描述
     */
	private String specMemo;
    /**
     * 所属卖家 0属于平台
     */
	private Long shopId;

	protected Serializable pkVal() {
		return this.id;
	}

}
