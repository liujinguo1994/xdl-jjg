package com.xdl.jjg.model.form;

import com.xdl.jjg.entity.Menus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "角色")
public class EsRoleForm implements Serializable {

    private static final long serialVersionUID = 2545884328132568794L;
    /**
     * 权限名称
     */
    @ApiModelProperty(required = true, value = "权限名称")
    @NotBlank(message = "权限名称不能为空")
    private String roleName;
    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String roleDescribe;

    /**
     * 角色所拥有的菜单权限
     */
    @ApiModelProperty(value = "角色所拥有的菜单权限")
    private List<Menus> menus;

}
