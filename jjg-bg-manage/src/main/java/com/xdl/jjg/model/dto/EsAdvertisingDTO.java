package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 广告位
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Data
public class EsAdvertisingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 图片名称
     */
    private String picName;

    /**
     * 位置
     */
    private String location;

    /**
     * 连接
     */
    private String link;

    /**
     * 图片地址
     */
    private String picUrl;

}
