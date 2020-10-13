package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: AfterSaleTypeVO
 * @Description: 售后类型VO
 * @Author: libw  981087977@qq.com
 * @Date: 7/16/2019 20:01
 * @Version: 1.0
 */
@Data
@ApiModel(description = "售后类型VO")
public class AfterSaleTypeVO implements Serializable {

    @ApiModelProperty(value = "退款可用状态")
    private Integer refundStatus;

    @ApiModelProperty(value = "退货可用状态")
    private Integer returnGoodsStatus;

    @ApiModelProperty(value = "换货可用状态")
    private Integer exchangeGoodsStatus;

    @ApiModelProperty(value = "维修可用状态")
    private Integer maintainStatus;
}
