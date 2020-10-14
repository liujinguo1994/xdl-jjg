package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

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
public class EsShopMenuForm implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 菜单父id
     */
    @ApiModelProperty(value = "菜单父ID")
	private Long parentId;

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
     * 权限表达式
     */
	@ApiModelProperty(value = "权限表达式")
	private String authRegular;

    /**
     * 菜单级别标识
     */
	@ApiModelProperty(value = "菜单级别标识")
	private String path;

    /**
     * 菜单级别
     */
	@ApiModelProperty(value = "菜单级别")
	private Integer grade;


}
