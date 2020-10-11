package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 自定义分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Data
@ToString
public class EsCustomCategoryDTO implements Serializable {
	private static final long serialVersionUID = 2252257754638258859L;
	/**
     * 主键ID
     */
	private Long id;

    /**
     * 自定义分类名称
     */
	private String categoryName;

    /**
     * 所属专区
     */
	private Long zoneId;

}
