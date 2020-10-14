package com.xdl.jjg.shiro.oath;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 */
@Data
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;

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
     * 联系方式
     */
    private String mobile;
    /**
     * 是否为超级管理员
     */
    private Integer isAdmin;
    /**
     * 管理员真实姓名
     */
    private String realName;
    /**
     * 头像
     */
    private String face;
    /**
     * 用户的权限集合
     */
    private List<String> urlSet;

    /**
     * 用户的角色集合
     */
    private List<String> roles;


}