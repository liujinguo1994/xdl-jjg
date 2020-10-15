package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单主表-es_trade
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsBuyerItemsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
	@ApiModelProperty(required = false,value = "交易编号")
	private String tradeSn;

	/**
	 * 订单编号
	 */
	@ApiModelProperty(required = false,value = "订单编号")
	private String orderSn;

	/**
	 * 系统配置 未支付 自动取消时间
	 */
	@ApiModelProperty(required = false,value = "系统配置 未支付 自动取消时间")
	private Long closeOrderTime;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(required = false,value = "店铺ID")
	private Long shopId;
	/**
	 * 店铺名称
	 */
	@ApiModelProperty(required = false,value = "店铺名称")
	private String shopName;

	/**
	 * 订单状态
	 */
	@ApiModelProperty(value = "订单状态 CONFIRM:待付款,PAID_OFF:待发货,SHIPPED:待收货，COMPLETE:已完成 ，ROG:已收货，CANCELLED:已取消 ，AFTER_SERVICE:售后中")
	private String orderState;

	/**
	 * 订单创建时间
	 */
	@ApiModelProperty(value = "订单创建时间")
	private Long createTime;
	/**
	 * 付款状态
	 */
	@ApiModelProperty(value = "付款状态")
	private String payState;
	/**
	 * 货运状态
	 */
	@ApiModelProperty(value = "货运状态")
	private String shipState;
	/**
	 * 售后状态
	 */
	@ApiModelProperty(value = "售后状态")
	private String serviceState;
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
	 * 支付方式类型
	 */
	@ApiModelProperty(value = "支付方式类型")
	private String paymentType;
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

	@ApiModelProperty(value = "订单商品项集合")
	private List<EsBuyerOrderItemsVO> esOrderItemsVOList;


}
