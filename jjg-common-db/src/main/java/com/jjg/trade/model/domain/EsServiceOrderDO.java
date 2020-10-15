package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jjg.trade.model.vo.OrderOperateAllowable;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 售后专用DO
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data

public class EsServiceOrderDO extends Model<EsServiceOrderDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单主键
     */
	private Long id;
    /**
     * 父订单编号
     */
	private String tradeSn;
    /**
     * 子订单订单编号
     */
	private String orderSn;
    /**
     * 店铺ID
     */
	private Long shopId;
    /**
     * 店铺名称
     */
	private String shopName;
    /**
     * 会员ID
     */
	private Long memberId;
    /**
     * 买家姓名
     */
	private String memberName;
	/**
	 * 买家账号
	 */
	private String mobile;
    /**
     * 订单状态
     */
	private String orderState;
    /**
     * 付款状态
     */
	private String payState;
    /**
     * 货运状态
     */
	private String shipState;
    /**
     * 售后状态
     */
	private String serviceState;
	/**
	 * 售后类型
	 */
	private String serviceType;
    /**
     * 结算状态
     */
	private String billState;

    /**
     * 支付方式id
     */
	private Long paymentMethodId;
    /**
     * 支付方式名称
     */
	private String paymentMethodName;
    /**
     * 支付方式类型
     */
	private String paymentType;
    /**
     * 支付时间
     */
	private Long paymentTime;
    /**
     * 支付金额
     */
	private Double payMoney;
    /**
     * 订单总额
     */
	private Double orderMoney;
    /**
     * 余额支付
     */
	private Double payedMoney;
    /**
     * 商品总额(折扣后价格)
     */
	private Double goodsMoney;
    /**
     * 优惠金额
     */
	private Double discountMoney;
    /**
     * 配送费用
     */
	private Double shippingMoney;
	/**
	 * 需要第三方支付的金额
	 */
	private Double needPayMoney;
    /**
     * 是否被删除 0 未删除 1删除
     */
	private Integer isDel;
    /**
     * 商品数量
     */
	private Integer goodsNum;

    /**
     * 订单取消原因
     */
	private String cancelReason;

    /**
     * 完成时间
     */
	private Long completeTime;
    /**
     * 确认收货签收时间
     */
	private Long signingTime;
    /**
     * 发货时间
     */
	private Long shipTime;
    /**
     * 支付返回的交易号
     */
	private String payOrderNo;
    /**
     * 订单来源 (pc、wap、app)
     */
	private String clientType;
    /**
     * 是否需要发票,0：否，1：是
     */
	private Integer needReceipt;
    /**
     * 订单创建时间
     */
	private Long createTime;
    /**
     * 订单更新时间
     */
	private Long updateTime;

	/**
	 * 订单商品明细表信息 取消订单时使用
	 */
	private List<EsOrderItemsDO> esOrderItemsListDO;

	/**
	 * 订单商品明细表信息 申请售后时使用
	 */
	private EsOrderItemsDO esOrderItemsDO;

	/**
	 * 操作权限
	 */
	private OrderOperateAllowable orderOperateAllowable;

	/**
	 * 页面列表支付方式
	 */
	private String paymentMethod;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
