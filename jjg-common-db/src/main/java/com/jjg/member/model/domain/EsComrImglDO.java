package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 投诉举报图片表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-31
 */
@Data
public class EsComrImglDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 类型，0投诉，1举报
     */
	private Integer type;

    /**
     * 缩略图路径
     */
    private String thumbnail;
    /**
     * 小图路径
     */
    private String small;
    /**
     * 大图路径
     */
    private String big;
    /**
     * 原图路径
     */
    private String original;
    /**
     * 极小图路径
     */
    private String tiny;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 排序
     */
    private Long comId;


}
