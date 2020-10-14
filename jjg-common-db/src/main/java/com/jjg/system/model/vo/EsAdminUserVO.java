package com.jjg.member.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class EsAdminUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */

    @ApiModelProperty(required = false, value = "id")
    private Long id;

    /**
     * 管理员名称
     */
    @ApiModelProperty(required = false, value = "username")
    private String username;

    /**
     * 管理员密码
     */
    @ApiModelProperty(required = false, value = "password")
    private String password;

    /**
     * 盐
     */
    @ApiModelProperty(required = false, value = "salt")
    private String salt;

    /**
     * 部门
     */
    @ApiModelProperty(required = false, value = "department")
    private Long department;

    /**
     * 角色ID
     */
    @JsonSerialize(using = ToStringSerializer.class)

    @ApiModelProperty(required = false, value = "roleId")
    private Long roleId;

    /**
     * 创建日期
     */

    @ApiModelProperty(required = false, value = "createTime")
    private Long createTime;

    /**
     * 备注
     */
    @ApiModelProperty(required = false, value = "remark")
    private String remark;

    /**
     * 是否删除 0 未删除 1删除
     */

    @ApiModelProperty(required = false, value = "isDel")
    private Integer isDel;

    /**
     * 管理员真实姓名
     */

    @ApiModelProperty(required = false, value = "realName")
    private String realName;

    /**
     * 头像
     */
    @ApiModelProperty(required = false, value = "face")
    private String face;

    /**
     * 是否为超级管理员
     */

    @ApiModelProperty(required = false, value = "isAdmin")
    private Integer isAdmin;

    /**
     * 联系方式
     */
    @ApiModelProperty(required = false, value = "mobile")
    private String mobile;


    protected Serializable pkVal() {
        return this.id;
    }

}
