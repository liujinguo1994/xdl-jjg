package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-设置余额
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-02
 */
@Data
@ApiModel
public class EsAppletSetBalanceForm implements Serializable {
    private static final long serialVersionUID = 1111975606600091147L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true,value = "余额")
    @NotNull(message = "余额不能为空")
    private Double balance;
}
