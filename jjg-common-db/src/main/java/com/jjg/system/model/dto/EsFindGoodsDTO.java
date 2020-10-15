package com.jjg.system.model.dto;

import com.xdl.jjg.model.domain.EsFindGoodsGalleryDTO;
import lombok.Data;
import lombok.ToString;

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
@ToString
public class EsFindGoodsDTO implements Serializable {

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
    private List<EsFindGoodsGalleryDTO> galleryList;

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
    private Long createTime;

    /**
     * 最后修改时间
     */
    private Long updateTime;
}
