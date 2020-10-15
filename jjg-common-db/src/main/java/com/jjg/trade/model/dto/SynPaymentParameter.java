package com.jjg.trade.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 同步支付请求参数
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 14:28:41
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SynPaymentParameter implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "提交给第三方平台单号")
    private String outTradeNo;
    @ApiModelProperty(value = "第三方平台返回交易号")
    private String tradeNo;
    @ApiModelProperty(value = "支付金额")
    private String totalAmount;
    @ApiModelProperty(value = "支付宝 支付状态")
    private String tradeStatus;


    private Map<String, String[]> requestParams;
}
