package com.xdl.jjg.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;


/**
 * <p>
 *
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
@Data
@Accessors(chain = true)
public class EsRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */

    @ApiModelProperty(required = false, value = "id")
    private Long id;

    /**
     * 权限名称
     */

    @ApiModelProperty(required = false, value = "roleName")
    private String roleName;

    /**
     * 角色拼音名称
     */
    @ApiModelProperty(required = false, value = "name")
    private String name;

    /**
     * 权限集合
     */

    @ApiModelProperty(required = false, value = "authIds")
    private String authIds;

    /**
     * 角色描述
     */

    @ApiModelProperty(required = false, value = "roleDescribe")
    private String roleDescribe;


    protected Serializable pkVal() {
        return this.id;
    }

}
