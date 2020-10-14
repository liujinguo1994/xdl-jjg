package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
@ApiModel
public class EsRefunAuthForm {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    @NotBlank(message = "订单号必填")
    private String orderSn;

    /**
     * 维权单号
     */
    @ApiModelProperty(value = "退货(款)单编号" ,name = "sn",required = true)
    @NotBlank(message = "退款单号必填")
    private String sn;

    /**
     * 审核备注
     */
    @ApiModelProperty(value = "审核备注" ,name = "remake",required = true)
    private String remake;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 0 审核通过 1审核驳回" ,name = "authState",required = true)
    private String authState;
}
