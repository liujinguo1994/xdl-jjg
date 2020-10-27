package com.jjg.trade.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.trade.model.vo.ReceiptVO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单明细表-es_order
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsOrderDTO extends CartDTO implements Serializable {

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
     * 评论是否完成
     */
	private String commentStatus;
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
	 * 支付插件名称
	 */
	private String pluginId;
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
	 * 普通配送费用
	 */
	private Double  commonFreightPrice;
	/**
	 * 生鲜配送费
	 */
	private Double  freshFreightPrice;
	/**
	 * 0没退,1已经退了
	 */
	private Integer hasFresh;
	/**
	 * 需要第三方支付的金额
	 */
	private Double needPayMoney;

	/**
     * 收货人姓名
     */
	private String shipName;
    /**
     * 收货地址
     */
	private String shipAddr;
    /**
     * 收货人邮编
     */
	private String shipZip;
    /**
     * 收货人手机
     */
	private String shipMobile;
    /**
     * 收货人电话
     */
	private String shipTel;
	/**
	 * 配送方式（快递 自提）
	 */
	private String shipMethod;
    /**
     * 收货时间
     */
	private String receiveTime;
    /**
     * 配送地区-省份ID
     */
	private Long shipProvinceId;
    /**
     * 配送地区-城市ID
     */
	private Long shipCityId;
    /**
     * 配送地区-区(县)ID
     */
	private Long shipCountyId;
    /**
     * 配送街道id
     */
	private Long shipTownId;
    /**
     * 配送地区-省份
     */
	private String shipProvince;
    /**
     * 配送地区-城市
     */
	private String shipCity;
    /**
     * 配送地区-区(县)
     */
	private String shipCounty;
    /**
     * 配送街道
     */
	private String shipTown;
    /**
     * 是否被删除 0 未删除 1删除
     */
	private Integer isDel;
    /**
     * 商品数量
     */
	private Integer goodsNum;
    /**
     * 订单备注
     */
	private String remark;
    /**
     * 订单取消原因
     */
	private String cancelReason;
    /**
     * 货物列表json
     */
	private String itemsJson;
    /**
     * 会员收货地址ID
     */
	private Long addressId;
    /**
     * 管理员备注
     */
	private String adminRemark;
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
     * 是否需要发票,0：否，1：是 buyao
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
	 * 订单商品信息
	 */
	private List<EsOrderItemsDTO> esOrderItemsVO;
	/**
	 * 购物车SKU 集合
	 */
	//private List<CartItemsDTO> CartItemsList;

	private List<CartItemsDTO> cartItemsList;
    /**
     * 商品名称
     */
    private String goodsName;

	/**
	 * 发票信息
	 */
	private ReceiptVO receiptVO;

	/**
	 * 签约公司
	 */
	private Long companyId;

	/**
	 * 自提信息文本
	 */
	private String deliveryText;

	/**
	 * 自提时间
	 */
	private String deliveryTime;

	/**
	 * 申请退款时间
	 */
	private Long applyRefundTime;

	/**
	 * 用一个购物车购造订单
	 *
	 * @param cart
	 */
	public EsOrderDTO(CartDTO cart) {

		super(cart.getShopId(), cart.getShopName());

		// 初始化产品及优惠数据
		this.setWeight(cart.getWeight());
		this.setPrice(cart.getPrice());
		this.setCartItemsList(cart.getCartItemsList());
		this.setCouponList(cart.getCouponList());
		this.setGiftCouponList(cart.getGiftCouponList());
		this.setGiftList(cart.getGiftList());
		this.setGiftPoint(cart.getGiftPoint());

	}

	public EsOrderDTO(){}

	protected Serializable pkVal() {
		return this.id;
	}

}
