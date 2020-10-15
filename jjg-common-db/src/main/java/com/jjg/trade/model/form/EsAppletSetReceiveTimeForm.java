package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 小程序-设置送货时间
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-29
 */
@Data
@ApiModel
public class EsAppletSetReceiveTimeForm implements Serializable {
    private static final long serialVersionUID = -7374245046188501191L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true,value = "送货时间")
    @NotBlank(message = "送货时间")
    private String receiveTime;
}
