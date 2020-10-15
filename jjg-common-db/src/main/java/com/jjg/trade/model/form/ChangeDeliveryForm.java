package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("设置自提信息")
public class ChangeDeliveryForm implements Serializable {
    @ApiModelProperty("页面选中买家自提 1 是，2否")
    private Integer isDelivery;
    @ApiModelProperty("店铺ID")
    private Long shopId;
}
