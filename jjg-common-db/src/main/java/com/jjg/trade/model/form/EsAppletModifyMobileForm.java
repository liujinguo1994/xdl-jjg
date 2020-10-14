package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 小程序-修改手机号码
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-20 09:28:26
 */
@Data
@ApiModel
public class EsAppletModifyMobileForm implements Serializable {

    private static final long serialVersionUID = 2325000714027995078L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;
    /**
     * 手机号码
     */
    @ApiModelProperty(required = true, value = "手机号码")
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    /**
     * 验证码
     */
    @ApiModelProperty(required = true, value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;


}
