package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSellerMemberAdminVO implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty(required = false,value = "主键id",example = "1")
    private Long id;
    /**
     * 会员id
     */
    @ApiModelProperty(required = false,value = "会员id",example = "1")
    private Long memberId;
    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(required = false,value = "会员登陆用户名")
    private String name;
    /**
     * 邮箱
     */
    @ApiModelProperty(required = false,value = "邮箱")
    private String email;
    /**
     * 会员登陆密码
     */
    @ApiModelProperty(required = false,value = "会员登陆密码")
    private String password;
     /**
      * * 手机号码
     */
     @ApiModelProperty(required = false,value = "手机号码")
     private String mobile;
    /**
     * 角色id
     */
    @ApiModelProperty(required = false,value = "角色id")
    private Long roleId;
    /**
     * 店铺id
     */
    @ApiModelProperty(required = false,value = "店铺id")
    private Long shopId;
    /**
     * 是否为超级管理员，1为超级管理员 0为其他管理员
     */
    @ApiModelProperty(required = false,value = "1为超级管理员 0为其他管理员")
    private Integer isAdmin;


}
