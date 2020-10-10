package com.xdl.jjg.model.form;

import lombok.Data;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import java.util.Date;

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
public class EsRoleForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 权限名称
     */
	private String roleName;

    /**
     * 角色拼音名称
     */
	private String name;

    /**
     * 权限集合
     */
	private String authIds;

    /**
     * 角色描述
     */
	private String roleDescribe;

	protected Serializable pkVal() {
		return this.id;
	}

}
