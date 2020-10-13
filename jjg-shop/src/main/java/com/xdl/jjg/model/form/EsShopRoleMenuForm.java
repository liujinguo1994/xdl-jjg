package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺菜单管理
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-04
 */
@Data
@Api
public class EsShopRoleMenuForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单标题
     */
	@ApiModelProperty(value = "菜单标题")
	private String title;

    /**
     * 菜单唯一标识
     */
	@ApiModelProperty(value = "菜单唯一标识")
	private String identifier;
	/**
	 * 此菜单是否选中
	 */
	@ApiModelProperty(value = "此菜单是否选中")
	private boolean checked;
    /**
     * 权限表达式
     */
	@ApiModelProperty(value = "权限表达式")
	private String authRegular;

	/**
	 * 子菜单
	 */
	@ApiModelProperty(value = "子菜单")
	private List<EsShopRoleMenuForm> children;

}
