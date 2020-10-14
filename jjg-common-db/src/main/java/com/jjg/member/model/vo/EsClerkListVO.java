package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModelProperty;
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
public class EsClerkListVO implements Serializable {


    /**
     * 主键ID
     */
    @ApiModelProperty(required = false,value = "主键ID",example = "1")
    private Long id;
    /**
     * 会员id
     */
    @ApiModelProperty(required = false,value = "会员id",example = "1")
    private Long memberId;
    /**
     * 店员名称
     */
    @ApiModelProperty(required = false,value = "店员名称")
    private String clerkName;
    /**
     * 是否为超级管理员，1为超级管理员 0为其他管理员
     */
    @ApiModelProperty(required = false,value = "1为超级管理员 0为其他管理员")
    private Integer isAdmin;
    /**
     * 角色id
     */
    @ApiModelProperty(required = false,value = "角色id")
    private Long roleId;
    /**
     * 店员状态，0为正常，1为禁用
     */
    @ApiModelProperty(required = false,value = "0为正常，1为禁用")
    private Integer state;
    /**
     * 创建日期
     */
    @ApiModelProperty(required = false,value = "创建日期")
    private Long createTime;
    /**
     * 店铺id
     */
    @ApiModelProperty(required = false,value = "店铺id")
    private Long shopId;

    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(required = false,value = "会员登陆用户名")
    private String name;

    /**
     * 手机号码
     */
    @ApiModelProperty(required = false,value = "手机号码")
    private String mobile;
    /**
     * 邮箱
     */
    @ApiModelProperty(required = false,value = "邮箱")
    private String email;
    /**
     * 角色名称
     */
    @ApiModelProperty(required = false,value = "角色名称")
    private String roleName;


}
