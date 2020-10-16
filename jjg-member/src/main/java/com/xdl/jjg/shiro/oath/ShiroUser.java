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

    private String loginName;

    private String name;

    private String password;

    private String salt;

    private Integer sex;

    private String phone;

    private String email;

    private String companyCode;

    private List<String> urlSet;

    private List<String> roles;


}