package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 发现好货
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Data
public class EsFindGoodsDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 商品名称
     */
	private String goodsName;

    /**
     * 自定义分类id
     */
	private Long customCategoryId;

    /**
     * 商品链接
     */
	private String goodsUrl;

	/**
	 * 图片地址
	 */
	private String picUrl;

	/**
	 * 相册
	 */
	private List<EsFindGoodsGalleryDO> galleryList;

    /**
     * 商品id
     */
	private Long goodsId;

    /**
     * 商品描述
     */
	private String goodsDescription;

    /**
     * 创建时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 最后修改时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	/**
	 * 自定义分类名称
	 */
	private String categoryName;

	/**
	 * 商品价格
	 */
	private Double money;

	/**
	 * 浏览数量
	 */
	private Integer viewCount;
}
