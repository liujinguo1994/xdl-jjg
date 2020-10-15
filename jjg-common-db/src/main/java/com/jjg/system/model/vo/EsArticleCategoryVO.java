package com.jjg.system.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 文章分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@ApiModel
public class EsArticleCategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

    /**
     * 父分类id
     */
    @ApiModelProperty(value = "父分类id")
    private Long parentId;

    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    private String path;

    /**
     * 是否允许删除
     */
    @ApiModelProperty(value = "是否允许删除")
    private Integer allowDelete;

    /**
     * 分类类型
     */
    @ApiModelProperty(value = "分类类型")
    private String type;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 子分类
     */
    @ApiModelProperty(value = "子分类")
    private List<EsArticleCategoryVO> children;

}
