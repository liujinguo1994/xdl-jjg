package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺自定义分类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsCustomDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 店铺ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

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
	private Integer sort;

    /**
     * 分类图标
     */
	private String image;

	protected Serializable pkVal() {
		return this.id;
	}

}
