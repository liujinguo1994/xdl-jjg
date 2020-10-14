package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 公司折扣表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsDiscountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 公司ID
     */
	private Long companyId;

    /**
     * 商品分类ID
     */
	private Long categoryId;

	/**
	 * 商品分类名称
	 */
	private String categoryName;

    /**
     * 折扣（系数计算）
     */
	private Double discount;


}
