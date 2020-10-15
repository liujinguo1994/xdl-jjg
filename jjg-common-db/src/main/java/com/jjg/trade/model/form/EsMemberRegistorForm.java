package com.jjg.trade.model.form;

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
public class EsMemberRegistorForm implements Serializable {


    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(required = true, value = "会员登陆用户名")
    private String name;
    /**
     * 会员密码
     */
    @ApiModelProperty(required = true,value = "会员密码")
    private String password;
    /**
     * 手机号码
     */
    @ApiModelProperty(required = true, value = "手机号码")
    private String mobile;
    /**
     * 企业标识符
     */
    @ApiModelProperty(required = false,value = "企业标识符")
    private String companyCode;
    /**
     * 判断是否是个人还是企业注册 0个人，1企业
     */
    @ApiModelProperty(required = false,value = "判断是否是个人,0个人，1企业",example = "1")
    private Integer type;
    /**
     * 短信验证码
     */
    @ApiModelProperty(required = false,value = "验证码")
    private String code;

}
