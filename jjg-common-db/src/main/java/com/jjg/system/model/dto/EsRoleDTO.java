package com.jjg.member.model.dto;

import com.shopx.system.api.model.domain.vo.Menus;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsRoleDTO implements Serializable {

    private static final long serialVersionUID = 5147329712480485173L;
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
    /**
     * 角色所拥有的菜单权限
     */
    private List<Menus> menus;
}
