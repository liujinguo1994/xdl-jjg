package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店员
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsClerkDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 会员id
     */
	private Long memberId;

    /**
     * 店员名称
     */
	private String clerkName;

    /**
     * 是否为超级管理员，1为超级管理员 0为其他管理员
     */
	private Integer isAdmin;

    /**
     * 角色id
     */
	private Long roleId;

    /**
     * 店员状态，0为正常，1为禁用
     */
	private Integer state;

    /**
     * 创建日期
     */
	private Long createTime;

    /**
     * 店铺id
     */
	private Long shopId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 会员登陆密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String mobile;

}
