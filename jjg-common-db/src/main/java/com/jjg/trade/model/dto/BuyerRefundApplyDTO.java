package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 退款申请VO
 * @author zjp
 * @version v7.0
 * @since v7.0 上午10:33 2018/5/2
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class BuyerRefundApplyDTO implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;
    /**
     * 订单编号必填
     */
    private String orderSn;

    /**
     * 货品idList
     */
    private List<Integer> skuIdList;

    private Long skuId;
    /**
     * 会员ID
     */
    private Long memberId;

    @ApiModelProperty(name = "url", value = "图片路径",required = true)
    private List<BuyerUrlDTO> urlList;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 退货数量
     */
    private Integer returnNum;

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
     * 退(货)款类型( 退货-> 注释：就是退货 退款, 退款-> 注释：未发货，换货)
     */
    private String refundType;
    /**
     * 维权类型(取消订单,申请售后)
     */
    private String refuseType;
    /**
     * 退款单号
     */
    private String refundSn;

}
