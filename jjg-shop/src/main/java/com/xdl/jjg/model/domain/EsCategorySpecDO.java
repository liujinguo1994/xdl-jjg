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
public class EsCategorySpecDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
	private Long categoryId;
    /**
     * 规格id
     */
	private Long specId;


}
