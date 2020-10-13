package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-设置收货地址
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-29
 */
@Data
@ApiModel
public class EsSetAddressForm implements Serializable {
    private static final long serialVersionUID = 7843869314356537548L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true,value = "收货地址id")
    @NotNull(message = "收货地址id不能为空")
    private Long addressId;

}
