package com.jjg.member.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺菜单管理
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsShopMenuListDO implements Serializable {


    /**
     * 店铺菜单id
     */
	private Long id;
    /**
     * 菜单父id
     */
	private Long parentId;
    /**
     * 菜单标题
     */
	private String title;
    /**
     * 菜单唯一标识
     */
	private String identifier;
    /**
     * 权限表达式
     */
	private String authRegular;

    /**
     * 解绑标识1删除0未删除
     */
	private Integer isDel;
    /**
     * 菜单级别标识
     */
	private String path;
    /**
     * 菜单级别
     */
	private Integer grade;

    /**
     * 子菜单
     */
    private List<EsShopMenuListDO> children;

}
