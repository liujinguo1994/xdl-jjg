package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class EsAppletDeliveryMessageForm implements Serializable {

    private static final long serialVersionUID = 3288725768853974274L;

    @ApiModelProperty(required = true,value = "自提点ID")
    @NotNull(message = "自提点ID不能为空")
    private Long DeliveryId;

    @ApiModelProperty(required = true,value = "自提点日期ID")
    @NotNull(message = "日期ID不能为空")
    private Long DateId;

    @ApiModelProperty(required = true,value = "自提时间点ID")
    @NotNull(message = "时间ID不能为空")
    private Long TimeId;

    @ApiModelProperty(required = true,value = "店铺id")
    @NotNull(message = "店铺id不能为空")
    private Long shopId;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;
}
