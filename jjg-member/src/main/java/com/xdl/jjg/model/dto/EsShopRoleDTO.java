package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺角色
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsShopRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

	protected Serializable pkVal() {
		return this.id;
	}

}
