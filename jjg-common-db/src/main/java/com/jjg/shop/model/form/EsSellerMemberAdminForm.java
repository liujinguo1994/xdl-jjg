package com.jjg.shop.model.form;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author waf 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@Api
public class EsSellerMemberAdminForm implements Serializable {

    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(value = "用户名")
    private String clerkName;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 会员登陆密码
     */
    @ApiModelProperty(value = "登陆密码")
    private String password;
     /**
      * * 手机号码
     */
     @ApiModelProperty(value = "手机号码")
    private String mobile;
    /**
     * 角色id
     */
    @ApiModelProperty(value ="角色id" )
    private Long roleId;

    /**
     * 是否为超级管理员，1为超级管理员 0为其他管理员
     */
    @ApiModelProperty(value = "是否为超级管理员，1为超级管理员 2为其他管理员")
    private Integer isAdmin;


}
