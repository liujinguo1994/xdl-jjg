package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺自定义分类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsCustomDO implements Serializable {


    /**
     * 主键ID
     */
	private Long id;
    /**
     * 店铺ID
     */
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

    /**
     * 分类子列表
     */
    private List<EsCustomDO> children;



}
