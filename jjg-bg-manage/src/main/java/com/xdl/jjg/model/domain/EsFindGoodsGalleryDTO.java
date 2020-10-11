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
public class EsFindGoodsGalleryDTO implements Serializable {

    /**
     * 图片路径
     */
    private String url;
}
