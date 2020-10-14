package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "菜单")
public class EsMenuVO implements Serializable {

    private static final long serialVersionUID = -5865646279802044659L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private Long parentId;
    /**
     * 菜单标题
     */
    @ApiModelProperty(value = "菜单标题")
    private String title;
    /**
     * 菜单url
     */
    @ApiModelProperty(value = "菜单url")
    private String url;
    /**
     * 菜单唯一标识
     */
    @ApiModelProperty(value = "菜单唯一标识")
    private String identifier;
    /**
     * 权限表达式
     */
    @ApiModelProperty(value = "权限表达式")
    private String authExpression;
    /**
     * 删除标记 0 未删除 1删除
     */
    @ApiModelProperty(value = "删除标记")
    private Integer isDel;
    /**
     * 菜单级别标识
     */
    @ApiModelProperty(value = "菜单级别标识")
    private String path;
    /**
     * 菜单级别
     */
    @ApiModelProperty(value = "菜单级别")
    private Integer grade;

    /**
     * 子菜单
     */
    @ApiModelProperty(value = "子菜单")
    private List<EsMenuVO> children;

}
