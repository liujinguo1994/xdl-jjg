package com.jjg.member.model.dto;

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
public class EsBrandDTO  implements Serializable {
	private Long id;
    /**
     * 品牌名称
     */
	private String name;
    /**
     * 品牌图标
     */
	private String logo;

}
