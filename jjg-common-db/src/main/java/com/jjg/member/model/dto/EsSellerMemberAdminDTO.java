package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsSellerMemberAdminDTO implements Serializable {

    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 会员登陆用户名
     */
    private String name;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 会员登陆密码
     */
    private String password;
     /**
      * * 手机号码
     */
    private String mobile;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 是否为超级管理员，1为超级管理员 0为其他管理员
     */
    private Integer isAdmin;
    /**
     * 注册IP地址
     */
    private String registerIp;


}
