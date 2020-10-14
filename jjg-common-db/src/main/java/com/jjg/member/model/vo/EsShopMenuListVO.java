package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class EsShopMenuListVO implements Serializable {


    /**
     * 店铺菜单id
     */
    @ApiModelProperty(required = false,value = "店铺菜单id",example = "1")
    private Long id;
    /**
     * 菜单父id
     */
    @ApiModelProperty(required = false,value = "菜单父id",example = "1")
    private Long parentId;
    /**
     * 菜单标题
     */
    @ApiModelProperty(required = false,value = "菜单标题")
    private String title;
    /**
     * 菜单唯一标识
     */
    @ApiModelProperty(required = false,value = "菜单唯一标识")
    private String identifier;
    /**
     * 权限表达式
     */
    @ApiModelProperty(required = false,value = "权限表达式")
    private String authRegular;

    /**
     * 解绑标识1删除0未删除
     */
    @ApiModelProperty(required = false,value = "解绑标识1删除0未删除")
	private Integer isDel;
    /**
     * 菜单级别标识
     */
    @ApiModelProperty(required = false,value = "菜单级别标识")
    private String path;
    /**
     * 菜单级别
     */
    @ApiModelProperty(required = false,value = "菜单级别")
    private Integer grade;

    /**
     * 子菜单
     */
    @ApiModelProperty(required = false,value = "子菜单")
    private List<EsShopMenuListVO> children;

}
