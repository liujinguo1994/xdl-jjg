package com.xdl.jjg.model.form;

import com.shopx.trade.api.model.domain.vo.ReceiptVO;
import com.shopx.trade.api.model.enums.PaymentTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class CheckoutParamForm implements Serializable {

    @ApiModelProperty(value = "收货地址id")
    private Long addressId;

    @ApiModelProperty(value = "支付方式")
    private PaymentTypeEnum paymentType;

    @ApiModelProperty(value = "发票信息")
    private ReceiptVO receipt;

    @ApiModelProperty(value = "收货时间")
    private String receiveTime;

    @ApiModelProperty(value = "订单备注")
    private String remark;

    @ApiModelProperty(value = "客户端类型")
    private String clientType;

    @ApiModelProperty(value = "使用余额")
    private BigDecimal balance;
}
