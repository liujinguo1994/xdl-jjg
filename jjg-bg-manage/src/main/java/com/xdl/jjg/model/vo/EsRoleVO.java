package com.jjg.member.model.vo;

import com.xdl.jjg.entity.Menus;
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
@ApiModel(description = "角色")
public class EsRoleVO implements Serializable {
    private static final long serialVersionUID = 6166092861045843021L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    private String roleName;
    /**
     * 权限集合
     */
    @ApiModelProperty(value = "权限集合")
    private String authIds;
    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String roleDescribe;
    /**
     * 角色拼音名称
     */
    @ApiModelProperty(value = "角色拼音名称")
    private String name;
    /**
     * 角色所拥有的菜单权限
     */
    @ApiModelProperty(value = "角色所拥有的菜单权限")
    private List<Menus> menus;
}
