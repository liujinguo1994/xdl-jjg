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
public class EsRoleDO implements Serializable {
    private static final long serialVersionUID = -2344919629682739280L;
    /**
     * 主键ID
     */
	private Long id;
    /**
     * 权限名称
     */
	private String roleName;
    /**
     * 权限集合
     */
	private String authIds;
    /**
     * 角色描述
     */
	private String roleDescribe;
    /**
     * 角色拼音名称
     */
    private String name;
}
