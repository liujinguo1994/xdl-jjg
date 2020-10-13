package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_refund")
public class EsRefund extends Model<EsRefund> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 退货(款)单编号
     */
    private String sn;
    /**
     * 会员id
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 会员名称
     */
    @TableField("member_name")
    private String memberName;
    /**
     * 卖家id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 卖家姓名
     */
    @TableField("shop_name")
    private String shopName;
    /**
     * 订单编号
     */
    @TableField("order_sn")
    private String orderSn;
    /**
     * 退(货)款状态(退货中、申请通过、审核拒绝、退货入库、待人工处理、申请取消、退款中、退款失败、完成)
     */
    @TableField("refund_status")
    private String refundStatus;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    /**
     * 退款金额
     */
    @TableField("refund_money")
    private Double refundMoney;
    /**
     * 退还积分
     */
    @TableField("refund_point")
    private Double refundPoint;
    /**
     * 退款方式(原路退回，在线支付)
     */
    @TableField("refund_way")
    private String refundWay;
    /**
     * 退款账户类型(支付宝、银联、微信)
     */
    @TableField("account_type")
    private String accountType;
    /**
     * 退款账户
     */
    @TableField("return_account")
    private String returnAccount;
    /**
     * 客户备注
     */
    @TableField("customer_remark")
    private String customerRemark;
    /**
     * 客服备注
     */
    @TableField("seller_remark")
    private String sellerRemark;
    /**
     * 库管备注
     */
    @TableField("warehouse_remark")
    private String warehouseRemark;
    /**
     * 财务备注
     */
    @TableField("finance_remark")
    private String financeRemark;
    /**
     * 退款原因
     */
    @TableField("refund_reason")
    private String refundReason;
    /**
     * 拒绝原因
     */
    @TableField("refuse_reason")
    private String refuseReason;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 银行账号
     */
    @TableField("bank_account_number")
    private String bankAccountNumber;
    /**
     * 银行开户名
     */
    @TableField("bank_account_name")
    private String bankAccountName;
    /**
     * 银行开户行
     */
    @TableField("bank_deposit_name")
    private String bankDepositName;
    /**
     * 支付交易编号
     */
    @TableField("trade_sn")
    private String tradeSn;
    /**
     * 售后类型(已支付取消订单,申请售后)
     */
    @TableField("refuse_type")
    private String refuseType;
    /**
     * 退款支付结果交易号
     */
    @TableField("pay_order_no")
    private String payOrderNo;
    /**
     * 退(货)款类型(退货，退款)
     */
    @TableField("refund_type")
    private String refundType;
    /**
     * 订单类型(在线支付)
     */
    @TableField("payment_type")
    private String paymentType;
    /**
     * 退款失败原因
     */
    @TableField("refund_fail_reason")
    private String refundFailReason;
    /**
     * 退款时间
     */
    @TableField("refund_time")
    private Long refundTime;
    /**
     * 赠品信息
     */
    @TableField("refund_gift")
    private String refundGift;
    /**
     * 支付退款金额(除去余额支付外已退款的钱)
     */
    @TableField("refund_pay_price")
    private Double refundPayPrice;
    /**
     * 退还优惠券
     */
    @TableField("coupon_id")
    private Long couponId;

    /**
     * 处理状态
     */
    @TableField("process_status")
    private String processStatus;

    /**
     * 售后图片路径
     */
    @TableField("url")
    private String url;

    @TableField("ship_name")
    private String shipName;
    @TableField("is_quantity")
    private Long isQuantity;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
