package com.jjg.system.model.form;


import com.jjg.trade.model.enums.OrderStatusEnum;
import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
@ApiModel("商品销售明细")
public class EsGoodsSalesDetailForm extends QueryPageForm {
    @ApiModelProperty(value = "分类ID", example = "0")
    private Long categoryId;
    @ApiModelProperty(value = "店铺ID", example = "0")
    private Long shopId;
    @ApiModelProperty(value = "查询周期", example = "2020-05")
    private String queryCycle;
    @ApiModelProperty(value = "查询周期单位（ YEAR按年 MONTH按月）", example = "YEAR")
    private String queryCycleUnit;
    @ApiModelProperty(value = "订单状态（默认为已完成订单）", hidden = true)
    private String orderState = OrderStatusEnum.COMPLETE.value();
}
