package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 文章分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@ToString
public class EsArticleCategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类id
     */
    private Long parentId;

    /**
     * 路径
     */
    private String path;

    /**
     * 是否允许删除1允许 0不允许
     */
    private Integer allowDelete;

    /**
     * 分类类型
     */
    private String type;

    /**
     * 排序
     */
    private Integer sort;
}
