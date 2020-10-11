package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限vo 用于权限菜单的参数传递及展示
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class Menus implements Serializable {
    /**
     * 菜单标题
     */
    @ApiModelProperty(value = "菜单标题")
    private String title;
    /**
     * 菜单唯一标识
     */
    @ApiModelProperty(value = "菜单唯一标识")
    private String identifier;
    /**
     * 此菜单是否选中
     */
    @ApiModelProperty(value = "此菜单是否选中")
    private boolean checked;
    /**
     * 菜单的权限表达式
     */
    @ApiModelProperty(value = "菜单的权限表达式")
    private String authExpression;

    /**
     * 子菜单
     */
    @ApiModelProperty(value = "子菜单")
    private List<Menus> children;
}
