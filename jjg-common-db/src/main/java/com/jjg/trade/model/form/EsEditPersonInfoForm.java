package com.jjg.trade.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 编辑个人信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsEditPersonInfoForm implements Serializable {


    /**
     * 会员头像
     */
    @ApiModelProperty(required = false, value = "会员头像")
    private String face;
    /**
     * 登录名
     */
    @ApiModelProperty(required = false, value = "登录名")
    private String nickname;
    /**
     * 会员性别(1：男，2：女)
     */
    @ApiModelProperty(required = false, value = "会员性别(1：男，2：女)", example = "1")
    private Integer sex;
    /**
     * 会员生日
     */
    @ApiModelProperty(required = false, value = "会员生日", example = "1")
    private Long birthday;
    /**
     * 邮箱
     */
    @ApiModelProperty(required = false, value = "邮箱", example = "1")
    private String email;
    /**
     * 真实姓名
     */
    @ApiModelProperty(required = false, value = "真实姓名")
    private String cardName;



}
