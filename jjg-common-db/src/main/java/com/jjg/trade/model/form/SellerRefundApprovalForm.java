package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 卖家审核退(款)货VO
 * @author zjp
 * @version v7.0
 * @since v7.0 上午11:21 2018/5/2
 */
@Data
public class SellerRefundApprovalForm implements Serializable{
    @ApiModelProperty(value = "退款单号",name = "sn",required = true)
    private String sn;
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @ApiModelProperty(value = "是否同意退款:同意 1，不同意 2",allowableValues = "1,2",required = true)
    private Integer agree;
    @ApiModelProperty(value = "退款金额",name = "refund_price",required = true)
    private Double refundPrice;
    @ApiModelProperty(value = "退款备注" ,name = "remark",required = false)
    private String remark;
    @ApiModelProperty(value = "退还积分",name = "refund_point",required = false)
    private Integer refundPoint;

}
