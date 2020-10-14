package com.jjg.member.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.shopx.trade.api.model.domain.dto.EsOrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 交易VO
 * @author Snow create in 2018/4/9
 * @version v2.0
 * @since v7.0.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EsLfcTradeVO implements Serializable {

    private static final long serialVersionUID = -8580648928412433120L;

    @ApiModelProperty(name="trade_sn",value = "交易编号")
    private String tradeSn;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "会员昵称")
    private String memberName;

    @ApiModelProperty(value = "支付方式")
    private String paymentType;

    @ApiModelProperty(value = "价格信息")
    private PriceDetailVO priceDetail;

    @ApiModelProperty(value = "收货人信息")
    private EsConsigneeVO consignee;



    @ApiModelProperty(value = "订单集合")
    private List<EsOrderDTO> orderList;


    /**是否为充值*/
    @ApiModelProperty(value = "交易类型,0：订单，1：充值")
    private Integer isDeposit;

    @ApiModelProperty(name = "need_pay_money", value = "应付金额", required = false)
    private Double needPayMoney;
    @ApiModelProperty(name = "is_wn_order", value = "是否是微能订单,0不是，1是", required = false)
    private Integer isWnOrder;


    public String getTradeSn() {
        return tradeSn;
    }

    public void setTradeSn(String tradeSn) {
        this.tradeSn = tradeSn;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public PriceDetailVO getPriceDetail() {
        return priceDetail;
    }

    public void setPriceDetail(PriceDetailVO priceDetail) {
        this.priceDetail = priceDetail;
    }

    public EsConsigneeVO getConsignee() {
        return consignee;
    }

    public void setConsignee(EsConsigneeVO consignee) {
        this.consignee = consignee;
    }


    public List<EsOrderDTO> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<EsOrderDTO> orderList) {
        this.orderList = orderList;
    }


    public Integer getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(Integer isDeposit) {
        this.isDeposit = isDeposit;
    }

    public Double getNeedPayMoney() {
        return needPayMoney;
    }

    public void setNeedPayMoney(Double needPayMoney) {
        this.needPayMoney = needPayMoney;
    }

    public Integer getIsWnOrder() {
        return isWnOrder;
    }

    public void setIsWnOrder(Integer isWnOrder) {
        this.isWnOrder = isWnOrder;
    }

}
