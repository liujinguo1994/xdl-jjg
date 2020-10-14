package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺角色
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsShopRoleDO implements Serializable {


    /**
     * 角色主键
     */
	private Long id;
    /**
     * 角色名称
     */
	private String roleName;

    /**
     * 角色英文名称
     */
    private String englishName;
    /**
     * 角色权限
     */
	private String authIds;
    /**
     * 角色描述
     */
	private String roleDescribe;
    /**
     * 店铺id
     */
	private Long shopId;



}
