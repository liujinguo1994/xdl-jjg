package com.jjg.member.model.vo;

import com.shopx.trade.api.model.enums.PaymentTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 结算参数VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-07-01
 */
@Data
@ApiModel(description = "结算参数")
public class CheckoutParamVO implements Serializable {

    @ApiModelProperty(name = "address_id", value = "收货地址id")
    private Long addressId;

    @ApiModelProperty(name = "payment_type", value = "支付方式")
    private PaymentTypeEnum paymentType;

    @ApiModelProperty(value = "发票信息")
    private ReceiptVO receipt;

    @ApiModelProperty(name = "receive_time", value = "收货时间")
    private String receiveTime;

    @ApiModelProperty(value = "订单备注")
    private Map<Integer, String> remark;

    @ApiModelProperty(name = "client_type", value = "客户端类型")
    private String clientType;

    @ApiModelProperty(name = "delivery",value = "自提信息")
    private Map<Integer,String> deliveryMessageVOMap;
    private EsDeliveryMessageVO deliveryMessageVO;
}
