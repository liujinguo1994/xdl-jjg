package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@Accessors(chain = true)
public class EsArticleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 文章名称
     */
    private String articleName;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 文章排序
     */
    private Integer sort;

    /**
     * 外链url
     */
    private String outsideUrl;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 显示位置
     */
    private String showPosition;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

}
