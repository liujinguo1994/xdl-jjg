package com.xdl.jjg.model.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * applet-账密登录
 */
@Data
@ApiModel
public class EsAppletLoginForm implements Serializable {
    private static final long serialVersionUID = 1810543996020932630L;

    /**
     * 登陆密码
     */
    @ApiModelProperty(required = true,value = "登陆密码")
    @NotBlank(message = "登陆密码不能为空")
    private String password;

    /**
     * 手机号或者用户名
     */
    @ApiModelProperty(required = true,value = "手机号或者用户名")
    @NotBlank(message = "手机号或者用户名不能为空")
    private String nameOrMobile;

    /**
     * 临时登录凭证
     */
    @ApiModelProperty(required = true,value = "临时登录凭证")
    @NotBlank(message = "临时登录凭证不能为空")
    private String code;

}
