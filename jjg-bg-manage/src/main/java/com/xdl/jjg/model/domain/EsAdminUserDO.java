package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsAdminUserDO implements Serializable {

    private static final long serialVersionUID = -2018649653528890362L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 管理员名称
     */
    private String username;
    /**
     * 管理员密码
     */
    private String password;
    /**
     * 部门
     */
    private Long department;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 创建日期
     */
    private Long createTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否删除 0 未删除 1删除
     */
    private Integer isDel;
    /**
     * 管理员真实姓名
     */
    private String realName;
    /**
     * 头像
     */
    private String face;
    /**
     * 是否为超级管理员
     */
    private Integer isAdmin;
    /**
     * 权限名称
     */
    private String roleName;
    /**
     * 联系方式
     */
    private String mobile;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 部门路径
     */
    private String path;
    /**
     * 盐
     */
    private String salt;
}
