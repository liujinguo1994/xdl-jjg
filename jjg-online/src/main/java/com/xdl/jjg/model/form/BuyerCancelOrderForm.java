package com.xdl.jjg.model.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 退款申请
 * @author zjp
 * @version v7.0
 * @since v7.0 上午10:33 2018/5/2
 */
@Data
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BuyerCancelOrderForm implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;
    @ApiModelProperty(name = "orderSn", value = "订单编号",required = true)
    @NotBlank(message = "订单编号必填")
    private String orderSn;
    @ApiModelProperty(name = "refundReason", value = "退款原因",required = true)
    @NotBlank(message = "退款原因必填")
    private String refundReason;
    @ApiModelProperty(name = "customerRemark", value = "客户备注",required = false)
    private String customerRemark;
    @ApiModelProperty(name = "refundSn", value = "退款单号",required = false,hidden = true)
    private String refundSn;
    @ApiModelProperty(name = "url", value = "图片路径",required = true)
    private String url;
    /** add libw 2019/03/13 添加单品退款功能 start **/
    @ApiModelProperty(name = "orderGoodsIds", value = "订单商品id",required = false, hidden = true)
    @NotNull(message = "订单物品不能为空")
    private List<Integer> orderGoodsIds;
    /** add libw 2019/03/13 添加单品退款功能 end **/

//    @ApiModelProperty(name = "accountType", value = "账号类型 支付宝:ALIPAY, 微信:WEIXINPAY, 银行转账:BANKTRANSFER", allowableValues="ALIPAY,WEIXINPAY,BANKTRANSFER")
//    private String accountType;

//    @ApiModelProperty(name = "returnAccount", value = "退款账号",required = false)
//    private String returnAccount;


//    @ApiModelProperty(name = "bankName", value = "银行名称",required = false)
//    private String bankName;
//
//    @ApiModelProperty(name = "bankAccountNumber", value = "银行账号",required = false)
//    private String bankAccountNumber;
//
//    @ApiModelProperty(name = "bankAccountName",value = "银行开户名",required = false)
//    private String bankAccountName;
//
//    @ApiModelProperty(name = "bankDepositName", value = "银行开户行",required = false)
//    private String bankDepositName;



}
