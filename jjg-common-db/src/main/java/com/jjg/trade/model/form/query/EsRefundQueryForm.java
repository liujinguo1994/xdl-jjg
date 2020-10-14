package com.xdl.jjg.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsRefundQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退货(款)单编号
     */
	@ApiModelProperty(value = "退货(款)单编号")
	private String sn;

    /**
     * 会员id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "会员id")
	private Long memberId;

    /**
     * 会员名称
     */
	@ApiModelProperty(value = "会员名称")
	private String memberName;

    /**
     * 卖家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "卖家id")
	private Long shopId;

    /**
     * 卖家姓名
     */
	@ApiModelProperty(value = "卖家姓名")
	private String shopName;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 退(货)款状态(退货中、申请通过、审核拒绝、退货入库、待人工处理、申请取消、退款中、退款失败、完成)
     */
	@ApiModelProperty(value = "退(货)款状态(退货中、申请通过、审核拒绝、退货入库、待人工处理、申请取消、退款中、退款失败、完成)")
	private String refundStatus;

    /**
     * 创建时间
     */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

    /**
     * 退款金额
     */
	@ApiModelProperty(value = "退款金额")
	private Double refundMoney;

    /**
     * 退还积分
     */
	@ApiModelProperty(value = "退还积分")
	private Double refundPoint;

    /**
     * 退款方式(原路退回，在线支付)
     */
	@ApiModelProperty(value = "退款方式(原路退回，在线支付)")
	private String refundWay;

    /**
     * 退款账户类型(支付宝、银联、微信)
     */
	@ApiModelProperty(value = "退款账户类型(支付宝、银联、微信)")
	private String accountType;

    /**
     * 退款账户
     */
	@ApiModelProperty(value = "退款账户")
	private String returnAccount;

    /**
     * 客户备注
     */
	@ApiModelProperty(value = "客户备注")
	private String customerRemark;

    /**
     * 客服备注
     */
	@ApiModelProperty(value = "客服备注")
	private String sellerRemark;

    /**
     * 库管备注
     */
	@ApiModelProperty(value = "库管备注")
	private String warehouseRemark;

    /**
     * 财务备注
     */
	@ApiModelProperty(value = "财务备注")
	private String financeRemark;

    /**
     * 退款原因
     */
	@ApiModelProperty(value = "退款原因")
	private String refundReason;

    /**
     * 拒绝原因
     */
	@ApiModelProperty(value = "拒绝原因")
	private String refuseReason;

    /**
     * 银行名称
     */
	@ApiModelProperty(value = "银行名称")
	private String bankName;

    /**
     * 银行账号
     */
	@ApiModelProperty(value = "银行账号")
	private String bankAccountNumber;

    /**
     * 银行开户名
     */
	@ApiModelProperty(value = "银行开户名")
	private String bankAccountName;

    /**
     * 银行开户行
     */
	@ApiModelProperty(value = "银行开户行")
	private String bankDepositName;

    /**
     * 支付交易编号
     */
	@ApiModelProperty(value = "支付交易编号")
	private String tradeSn;

    /**
     * 售后类型(已支付取消订单,申请售后)
     */
	@ApiModelProperty(value = "售后类型(已支付取消订单,申请售后)")
	private String refuseType;

    /**
     * 退款支付结果交易号
     */
	@ApiModelProperty(value = "退款支付结果交易号")
	private String payOrderNo;

    /**
     * 退(货)款类型(退货，退款)
     */
	@ApiModelProperty(value = "退(货)款类型(退货，退款)")
	private String refundType;

    /**
     * 订单类型(在线支付)
     */
	@ApiModelProperty(value = "订单类型(在线支付)")
	private String paymentType;

    /**
     * 退款失败原因
     */
	@ApiModelProperty(value = "退款失败原因")
	private String refundFailReason;

    /**
     * 退款时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "退款时间")
	private Long refundTime;

    /**
     * 赠品信息
     */
	@ApiModelProperty(value = "赠品信息")
	private String refundGift;

    /**
     * 支付退款金额(除去余额支付外已退款的钱)
     */
	@ApiModelProperty(value = "支付退款金额(除去余额支付外已退款的钱)")
	private Double refundPayPrice;

    /**
     * 退还优惠券
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "退还优惠券")
	private Long couponId;

}
