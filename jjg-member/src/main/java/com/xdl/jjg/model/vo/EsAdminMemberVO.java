package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 后台会员列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@Api
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EsAdminMemberVO implements Serializable {

    /**
     * 会员id
     */
    @ApiModelProperty(required = false,value = "会员id",example = "1")
    private Long id;
    /**
     * 手机号
     */
    @ApiModelProperty(required = false,value = "手机号",example = "15056015220")
    private String mobile;
    /**
     * 邮箱
     */
    @ApiModelProperty(required = false,value = "邮箱",example = "1220316123@qq.com")
    private String email;
    /**
     * 会员等级
     */
    @ApiModelProperty(required = false,value = "会员等级")
    private String memberLevel;
    /**
     * 签约公司
     */
    @ApiModelProperty(required = false,value = "签约公司名称")
    private String company;
    /**
     * 账号余额
     */
    @ApiModelProperty(required = false,value = "账号余额")
    private Double money;
    /**
     * 注册时间
     */
    @ApiModelProperty(required = false,value ="注册时间",example = "123456789")
    private Long registerTime;
    /**
     *登陆时间
     */
    @ApiModelProperty(required = false,value = "登陆时间",example = "12345678")
    private Long loginTime;
    /**
     * 会员状态
     */
    @ApiModelProperty(required = false,value = "会员状态")
    private String state;
    /**
     * 成长值
     */
    @ApiModelProperty(required = false,value = "成长值",example = "1")
    private Integer grade;
    /**
     * 名称
     */
    @ApiModelProperty(required = false,value = "名称")
    private String name;


    @ApiModelProperty(required = false,value = "最后登录时间")
    private Long lastLogin;
}
