package com.xdl.jjg.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 卖家审核退(款)货VO
 * @author zjp
 * @version v7.0
 * @since v7.0 上午11:21 2018/5/2
 */
@Data
public class SellerRefundApprovalDTO implements Serializable{
    @ApiModelProperty(value = "退款单号",name = "sn",required = true)
    @NotBlank(message = "退款单号必填")
    private String sn;
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "是否同意退款:同意 1，不同意 0",allowableValues = "1,0",required = true)
    @NotNull(message = "是否同意必填")
    private Integer agree;

    @ApiModelProperty(value = "退款金额",name = "refund_price",required = true)
    @NotNull(message = "退款金额必填")
    private Double refundPrice;

    @ApiModelProperty(value = "退款备注" ,name = "remark",required = false)
    private String remark;
    @ApiModelProperty(value = "退还积分",name = "refund_point",required = false)
    private Integer refundPoint;

}
