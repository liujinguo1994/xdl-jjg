package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-设置自提
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-01
 */
@Data
@ApiModel
public class EsAppletChangeDeliveryForm implements Serializable {
    private static final long serialVersionUID = 8525518570016993704L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true,value = "店铺id")
    @NotNull(message = "店铺id不能为空")
    private Long shopId;

    @ApiModelProperty(required = true,value = "是否自提( 1 是，2否)")
    @NotNull(message = "是否自提不能为空")
    private Integer isDelivery;
}
