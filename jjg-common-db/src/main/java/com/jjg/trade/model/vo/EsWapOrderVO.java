package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单明细表-es_order
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-16
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsWapOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单主键
     */
	@ApiModelProperty(value = "订单主键")
	private Long id;
    /**
     * 父订单编号
     */
	@ApiModelProperty(value = "父订单编号")
	private String tradeSn;
    /**
     * 子订单订单编号
     */
	@ApiModelProperty(value = "子订单订单编号")
	private String orderSn;

	/**
	 * 系统配置 未支付 自动取消时间
	 */
	@ApiModelProperty(required = false,value = "系统配置 未支付 自动取消时间")
	private Long closeOrderTime;

    /**
     * 店铺ID
     */
	@ApiModelProperty(value = "店铺ID")
	private Long shopId;
    /**
     * 店铺名称
     */
	@ApiModelProperty(value = "店铺名称")
	private String shopName;
    /**
     * 会员ID
     */
	@ApiModelProperty(value = "会员ID")
	private Long memberId;
    /**
     * 买家姓名
     */
	@ApiModelProperty(value = "买家姓名")
	private String memberName;

	/**
	 * 买家账号
	 */
	@ApiModelProperty(value = "买家账号")
	private String mobile;
    /**
     * 订单状态
     */
	@ApiModelProperty(value = "订单状态 CONFIRM:待付款,PAID_OFF:待发货,SHIPPED:待收货，COMPLETE:已完成 ，ROG:已收货，CANCELLED:已取消 ，AFTER_SERVICE:售后中")
	private String orderState;
    /**
     * 付款状态
     */
	@ApiModelProperty(value = "付款状态 PAY_YES:已付款,PAY_NO:未付款")
	private String payState;
    /**
     * 货运状态
     */
	@ApiModelProperty(value = "货运状态 SHIP_NO:未发货 SHIP_YES:已发货 SHIP_ROG:已收货")
	private String shipState;
    /**
     * 售后状态
     */
	@ApiModelProperty(value = "售后状态 NOT_APPLY:未申请, APPLY:已申请 PASS:审核通过,REFUSE:审核未通过 EXPIRED:已失效或不允许申请售后")
	private String serviceState;
	/**
	 * 售后类型
	 */
	@ApiModelProperty(value = "售后类型")
	private String serviceType;
    /**
     * 结算状态
     */
	@ApiModelProperty(value = "结算状态")
	private String billState;
    /**
     * 评论是否完成
     */
	@ApiModelProperty(value = "评论是否完成")
	private String commentStatus;
    /**
     * 支付方式id
     */
	@ApiModelProperty(value = "支付方式id")
	private Long paymentMethodId;
    /**
     * 支付方式名称
     */
	@ApiModelProperty(value = "支付方式名称")
	private String paymentMethodName;
    /**
     * 支付方式类型
     */
	@ApiModelProperty(value = "支付方式类型")
	private String paymentType;

	/**
	 * 支付插件名称
	 */
	@ApiModelProperty(required = false,value = "支付插件名称")
	private String pluginId;
    /**
     * 支付时间
     */
	@ApiModelProperty(value = "支付时间")
	private Long paymentTime;
    /**
     * 支付金额
     */
	@ApiModelProperty(value = "支付金额")
	private Double payMoney;
    /**
     * 订单总额
     */
	@ApiModelProperty(value = "订单总额")
	private Double orderMoney;
    /**
     * 余额支付
     */
	@ApiModelProperty(value = "余额支付")
	private Double payedMoney;
    /**
     * 商品总额(折扣后价格)
     */
	@ApiModelProperty(value = "商品总额(折扣后价格)")
	private Double goodsMoney;
    /**
     * 优惠金额
     */
	@ApiModelProperty(value = "优惠金额")
	private Double discountMoney;
    /**
     * 配送费用
     */
	@ApiModelProperty(value = "配送费用")
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
	 * 0没退,1已经退了
	 */
	private Integer hasComm;
    /**
     * 收货人姓名
     */
	@ApiModelProperty(value = "收货人姓名")
	private String shipName;
    /**
     * 收货地址
     */
	@ApiModelProperty(value = "收货地址")
	private String shipAddr;
    /**
     * 收货人邮编
     */
	@ApiModelProperty(value = "收货人邮编")
	private String shipZip;
    /**
     * 收货人手机
     */
	@ApiModelProperty(value = "收货人手机")
	private String shipMobile;
    /**
     * 收货人电话
     */
	@ApiModelProperty(value = "收货人电话")
	private String shipTel;
	/**
	 * 配送方式（快递 自提）
	 */
	@ApiModelProperty(value = "配送方式（快递 自提）")
	private String shipMethod;
    /**
     * 收货时间
     */
	@ApiModelProperty(value = "收货时间")
	private String receiveTime;
    /**
     * 配送地区-省份ID
     */
	@ApiModelProperty(value = "配送地区-省份ID")
	private Long shipProvinceId;
    /**
     * 配送地区-城市ID
     */
	@ApiModelProperty(value = "配送地区-城市ID")
	private Long shipCityId;
    /**
     * 配送地区-区(县)ID
     */
	@ApiModelProperty(value = "配送地区-区(县)ID")
	private Long shipCountyId;
    /**
     * 配送街道id
     */
	@ApiModelProperty(value = "配送街道id")
	private Long shipTownId;
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
     * 是否被删除 0 未删除 1删除
     */
	@ApiModelProperty(value = "是否被删除 0 未删除 1删除")
	private Integer isDel;
    /**
     * 商品数量
     */
	@ApiModelProperty(value = "商品数量")
	private Integer goodsNum;
    /**
     * 订单备注
     */
	@ApiModelProperty(value = "订单备注")
	private String remark;
    /**
     * 订单取消原因
     */
	@ApiModelProperty(value = "订单取消原因")
	private String cancelReason;
    /**
     * 货物列表json
     */
	@ApiModelProperty(value = "货物列表json")
	private String itemsJson;
    /**
     * 会员收货地址ID
     */
	@ApiModelProperty(value = "会员收货地址ID")
	private Long addressId;
    /**
     * 管理员备注
     */
	@ApiModelProperty(value = "管理员备注")
	private String adminRemark;
    /**
     * 完成时间
     */
	@ApiModelProperty(value = "完成时间")
	private Long completeTime;
    /**
     * 确认收货签收时间
     */
	@ApiModelProperty(value = "确认收货签收时间")
	private Long signingTime;
    /**
     * 发货时间
     */
	@ApiModelProperty(value = "发货时间")
	private Long shipTime;
    /**
     * 支付返回的交易号
     */
	@ApiModelProperty(value = "支付返回的交易号")
	private String payOrderNo;
    /**
     * 订单来源 (pc、wap、app)
     */
	@ApiModelProperty(value = "订单来源 (pc、wap、app)")
	private String clientType;
    /**
     * 是否需要发票,0：否，1：是 buyao
     */
	@ApiModelProperty(value = "是否需要发票,0：否，1：是")
	private Integer needReceipt;
    /**
     * 订单创建时间
     */
	@ApiModelProperty(value = "订单创建时间")
	private Long createTime;
    /**
     * 订单更新时间
     */
	@ApiModelProperty(value = "订单更新时间")
	private Long updateTime;
	/**
	 * 发票信息
	 */
	@ApiModelProperty(value = "发票信息")
	private EsMemberReceiptHistoryVO esMemberReceiptHistoryVO;
	/**
	 * 订单商品信息
	 */
	@ApiModelProperty(value = "订单商品信息")
	private List<EsOrderItemsVO> esOrderItemsDO;


	/**
	 * 签约公司
	 */
	@ApiModelProperty(value = "签约公司ID")
	private Long companyId;

	/**
	 * 操作权限
	 */
	@ApiModelProperty(value = "操作权限")
	private OrderOperateAllowable orderOperateAllowable;

	/**
	 * 自提信息内容
	 */
	@ApiModelProperty(value = "自提信息内容")
	private String deliveryMessage;

	/**
	 * 订单状态 中文
	 */
	@ApiModelProperty(value = "订单状态 中文")
	private String orderStateText;

	/**
	 * 付款状态 中文
	 */
	@ApiModelProperty(value = "付款状态 中文")
	private String payStateText;
	/**
	 * 货运状态 中文
	 */
	@ApiModelProperty(value = "货运状态 中文")
	private String shipStateText;
	/**
	 * 售后状态 中文
	 */
	@ApiModelProperty(value = "售后状态 中文")
	private String serviceStateText;
	/**
	 * 取消订单时间
	 */
	@ApiModelProperty(value = "取消订单时间")
	private Long cancelTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
