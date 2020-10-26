package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.trade.api.constant.AfterSaleOperateAllowable;
import com.shopx.trade.api.model.domain.dto.BuyerUrlDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsRefundVO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 退货(款)单编号
     */
	@ApiModelProperty(value = "退货(款)单编号")
	private String sn;
    /**
     * 会员id
     */
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
	@ApiModelProperty(value = "审核状态 —》 APPLY:申请中,PASS:申请通过,REFUSE:审核拒绝,CANCEL:申请取消")
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
     * 维权类型(已支付取消订单,申请售后)
     */
	@ApiModelProperty(value = "(维权类型 -> : CANCEL_ORDER 取消订单,AFTER_SALE 申请售后)")
	private String refuseType;
    /**
     * 退款支付结果交易号
     */
	@ApiModelProperty(value = "退款支付结果交易号")
	private String payOrderNo;
    /**
     * 退(货)款类型(退货，退款)
     */
	@ApiModelProperty(value = "售后类型 -> RETURN_MONEY:退款,RETURN_GOODS:退货,CHANGE_GOODS:换货,REPAIR_GOODS:维修")
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
	@ApiModelProperty(value = "退还优惠券")
	private Long couponId;

	/**
	 * 处理状态
	 */
	@ApiModelProperty(value = "处理状态->TO_BE_PROCESS:待处理，WAIT_REFUND:待退款,REFUND_FAIL:退款失败,WAIT_IN_STORAGE:待入库,WAIT_SHIP:待发货,COMPLETED:完成")
	private String processStatus;

	/**
	 * 售后图片路径
	 */
	@ApiModelProperty(value = "售后图片路径")
	private String url;


	@ApiModelProperty(value = "操作权限")
	private AfterSaleOperateAllowable afterSaleOperateAllowable;

	@ApiModelProperty(value = "审核状态")
	private String refundStatusText;

	@ApiModelProperty(value = "图片集合")
	private List<BuyerUrlDTO> urlList;
	@ApiModelProperty(value = "退款账户类型")
	private String accountTypeText;
	@ApiModelProperty(value = "维权类型")
	private String refuseTypeText ;
	@ApiModelProperty(value = "处理状态")
	private String processStatusText;
	/**
	 * 售后商品信息
	 */
	private List<EsRefundGoodsVO> esRefundGoods;


	private List<String> processStatusList;
	protected Serializable pkVal() {
		return this.id;
	}
	private Long isQuantity;


	/**
	 * 配送地区-省份
	 */
	@ApiModelProperty(value = "配送地区-省份")
	private String shipProvince;
	/**
	 * 配送地区-城市
	 */
	@ApiModelProperty(value = "配送地区-城市")
	private String shipCity;
	/**
	 * 配送地区-区(县)
	 */
	@ApiModelProperty(value = "配送地区-区(县)")
	private String shipCounty;
	/**
	 * 配送街道
	 */
	@ApiModelProperty(value = "配送街道")
	private String shipTown;

	/**
	 * 收货地址
	 */
	@ApiModelProperty(value = "收货地址")
	private String shipAddr;
	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "收货人姓名")
	private String shipName;
	/**
	 * 收货人手机
	 */
	@ApiModelProperty(value = "收货人手机")
	private String shipMobile;
}
