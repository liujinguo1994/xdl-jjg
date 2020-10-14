package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 小程序设置登录密码
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-20 09:28:26
 */
@Data
@ApiModel
public class EsAppletSetPasswordForm implements Serializable {


    private static final long serialVersionUID = -143741955677345901L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    /**
     * 密码
     */
    @ApiModelProperty(required = true, value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;


}
