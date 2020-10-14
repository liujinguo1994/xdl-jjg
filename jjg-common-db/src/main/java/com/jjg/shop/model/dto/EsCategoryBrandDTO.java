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
public class EsCategoryBrandDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
	private Long categoryId;
    /**
     * 品牌id
     */
	private Long brandId;


}
