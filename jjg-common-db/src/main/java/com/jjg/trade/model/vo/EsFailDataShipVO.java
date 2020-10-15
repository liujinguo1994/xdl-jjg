package com.jjg.trade.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 导入失败发货详情
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-27
 */
@Data
@ApiModel
public class EsFailDataShipVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单号")
    private String orderSn;

    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    @ApiModelProperty(value = "快递公司名称")
    private String shipName;

    @ApiModelProperty(value = "快递单号")
    private String shipNo;


}