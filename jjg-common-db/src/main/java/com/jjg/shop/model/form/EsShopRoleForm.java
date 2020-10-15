package com.jjg.shop.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class EsShopRoleForm implements Serializable {

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;


    @ApiModelProperty(value = "角色描述")
    @NotBlank(message = "角色描述不能为空")
    private String roleDescribe;

    /**
     * 角色所拥有的菜单权限
     */
    @ApiModelProperty(name = "menus", value = "角色所拥有的菜单权限", required = true)
    private List<EsShopRoleMenuForm> menus;
}
