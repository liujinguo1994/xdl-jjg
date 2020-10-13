package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class EsRefundForm {

    @ApiModelProperty(value = "退货(款)单编号" ,name = "sn",required = true)
    @NotBlank(message = "退款单号必填")
    private String sn;

    @ApiModelProperty(value = "退款金额" ,name ="refund_price",required = true)
    @NotNull(message = "退款金额必填")
    private Double refundPrice;

    @ApiModelProperty(value = "退款备注",name ="remark",required = false)
    private String remark;



}
