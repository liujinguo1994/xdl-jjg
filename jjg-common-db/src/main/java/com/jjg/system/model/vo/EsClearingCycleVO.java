package com.jjg.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 结算周期设置
 */
@Data
@ApiModel
public class EsClearingCycleVO implements Serializable {

    private static final long serialVersionUID = -9200695552939547125L;

    @ApiModelProperty(value = "结算类型(1店铺结算,2供应商结算,3签约公司结算)")
    private Integer type;

    @ApiModelProperty(value = "结算周期类型(1月结，2季结，3半年)")
    private Integer cycleType;

    @ApiModelProperty(value = "出账日")
    private Integer billingDate;

}
