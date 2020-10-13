package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店员
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-28
 */
@Data
public class EsClerkListDO implements Serializable {


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
     * 会员登陆用户名
     */
    private String name;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 角色名称
     */
    private String roleName;


}
