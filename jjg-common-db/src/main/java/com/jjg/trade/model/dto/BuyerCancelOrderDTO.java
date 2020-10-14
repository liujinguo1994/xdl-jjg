package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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
public class BuyerCancelOrderDTO implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;

    /**
     * 订单编号必填
     */
    private String orderSn;
    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;
    /**
     * 退款原因
     */
    private String refundReason;
    /**
     * 账号类型 支付宝:ALIPAY, 微信:WEIXINPAY, 银行转账:BANKTRANSFER
     */
    private String accountType;
    /**
     * 退款账号
     */
    private String returnAccount;
    /**
     * 客户备注
     */
    private String customerRemark;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行账号
     */
    private String bankAccountNumber;
    /**
     * 银行开户名
     */
    private String bankAccountName;
    /**
     * 银行开户行
     */
    private String bankDepositName;
    /**
     * 退款单号
     */
    private String refundSn;
    /**
     * 订单物品不能为空
    */
    /** add libw 2019/03/13 添加单品退款功能 start **/
    private List<Integer> orderGoodsIds;
    /** add libw 2019/03/13 添加单品退款功能 end **/

}
