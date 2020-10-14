package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 发现好货相册
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Data
public class EsFindGoodsGalleryDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 发现好货ID
     */
    private Long findGoodsId;

    /**
     * 图片路径
     */
    private String url;
}
