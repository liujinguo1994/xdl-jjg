package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;


/**
 * <p>
 * 登录form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-26 10:18:18
 */
@Data
@ApiModel(description = "登录form")
public class EsLoginForm implements Serializable {

    private static final long serialVersionUID = -6356880843666551066L;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(required = true, value = "用户名")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(required = true, value = "密码")
    private String password;

    /**
     * 图片验证码
     */
    @NotBlank(message = "图片验证码不能为空")
    @ApiModelProperty(required = true, value = "图片验证码")
    private String captcha;

    /**
     * UUID
     */
    @NotBlank(message = "uuid不能为空")
    @ApiModelProperty(required = true, value = "uuid")
    private String uuid;
}
