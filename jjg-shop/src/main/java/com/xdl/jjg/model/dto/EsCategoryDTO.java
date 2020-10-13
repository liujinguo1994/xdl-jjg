package com.xdl.jjg.model.dto;

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
public class EsCategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 分类名称
     */
	private String name;
    /**
     * 分类父id
     */
	private Long parentId;
    /**
     * 分类父子路径
     */
	private String categoryPath;
    /**
     * 该分类下商品数量
     */
	private Integer goodsCount;
    /**
     * 分类排序
     */
	private Integer categoryOrder;
    /**
     * 分类图标
     */
	private String image;

	protected Serializable pkVal() {
		return this.id;
	}

}
