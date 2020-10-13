package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.shopx.trade.api.model.domain.EsBuyerOrderItemsDO;
import com.shopx.trade.api.model.domain.EsOrderDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zjp
 * @version v7.0
 * @Description 售后申请参数VO
 * @ClassName RefundApplyVO
 * @since v7.0 上午11:28 2018/7/4
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class RefundApplyVO {
    @ApiModelProperty(name = "original_way",value = "是否支持原路退回")
    private String originalWay;

    @ApiModelProperty(name = "return_money",value = "退款金额")
    private Double returnMoney;

    @ApiModelProperty(name = "return_point",value = "退款积分")
    private Integer returnPoint;

    @ApiModelProperty(name = "order",value = "订单明细")
    private EsOrderDO order;

    @ApiModelProperty(name = "sku_list",value = "货品列表")
    private List<EsBuyerOrderItemsDO> skuList;

}
