package com.jjg.shop.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Api
@Data
public class EsRefundStoreForm {

    @ApiModelProperty(value = "退货(款)单编号" ,name = "sn",required = true)
    @NotBlank(message = "退款单号必填")
    private String sn;


    @ApiModelProperty(value = "退款备注",name ="remark",required = false)
    private String remark;
    @ApiModelProperty(value = "是否退库存 1 是 2 否",name ="isQuantity",required = false)
    private Long isQuantity;


}
