package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.trade.api.model.domain.dto.EsCouponDTO;
import com.shopx.trade.api.model.domain.dto.EsOrderDTO;
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
public class EsTradeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(required = false,value = "主键ID")
	private Long id;
    /**
     * 订单编号
     */
	@ApiModelProperty(required = false,value = "订单编号")
	private String tradeSn;
    /**
     * 买家id
     */
	@ApiModelProperty(required = false,value = "买家id")
	private Long memberId;
    /**
     * 买家用户名
     */
	@ApiModelProperty(required = false,value = "买家用户名")
	private String memberName;
    /**
     * 支付方式id
     */
	@ApiModelProperty(required = false,value = "支付方式id")
	private Long paymentMethodId;
    /**
     * 支付方式名称
     */
	@ApiModelProperty(required = false,value = "支付方式名称")
	private String paymentMethodName;
    /**
     * 支付方式类型
     */
	@ApiModelProperty(required = false,value = "支付方式类型")
	private String paymentType;
    /**
     * 支付插件名称
     */
	@ApiModelProperty(required = false,value = "支付插件名称")
	private String pluginId;
    /**
     * 总价格
     */
	@ApiModelProperty(required = false,value = "总价格")
	private Double totalMoney;
    /**
     * 商品价格
     */
	@ApiModelProperty(required = false,value = "商品价格")
	private Double goodsMoney;
    /**
     * 运费
     */
	@ApiModelProperty(required = false,value = "运费")
	private Double freightMoney;
    /**
     * 优惠的金额
     */
	@ApiModelProperty(required = false,value = "优惠的金额")
	private Double discountMoney;
	/**
	 * 余额支付
	 */
	@ApiModelProperty(required = false,value = "余额支付（如果使用余额小于交易总额，则不对订单进行余额付款）")
	private Double useBalance;
    /**
     * 收货地址id
     */
	@ApiModelProperty(required = false,value = "收货地址id")
	private Long consigneeId;
    /**
     * 收货人姓名
     */
	@ApiModelProperty(required = false,value = "收货人姓名")
	private String consigneeName;
    /**
     * 收货国家
     */
	@ApiModelProperty(required = false,value = "收货国家")
	private String consigneeCountry;
    /**
     * 收货国家id
     */
	@ApiModelProperty(required = false,value = "收货国家id")
	private Long consigneeCountryId;
    /**
     * 收货省
     */
	@ApiModelProperty(required = false,value = "收货省")
	private String consigneeProvince;
    /**
     * 收货省id
     */
	@ApiModelProperty(required = false,value = "收货省id")
	private Long consigneeProvinceId;
    /**
     * 收货市
     */
	@ApiModelProperty(required = false,value = "收货市")
	private String consigneeCity;
    /**
     * 收货市id
     */
	@ApiModelProperty(required = false,value = "收货市id")
	private Long consigneeCityId;
    /**
     * 收货区
     */
	@ApiModelProperty(required = false,value = "收货区")
	private String consigneeCounty;
    /**
     * 收货区id
     */
	@ApiModelProperty(required = false,value = "收货区id")
	private Long consigneeCountyId;
    /**
     * 收货镇
     */
	@ApiModelProperty(required = false,value = "收货镇")
	private String consigneeTown;
    /**
     * 收货镇id
     */
	@ApiModelProperty(required = false,value = "收货镇id")
	private Long consigneeTownId;
    /**
     * 收货详细地址
     */
	@ApiModelProperty(required = false,value = "收货详细地址")
	private String consigneeAddress;
    /**
     * 收货人手机号
     */
	@ApiModelProperty(required = false,value = "收货人手机号")
	private String consigneeMobile;
    /**
     * 收货人电话
     */
	@ApiModelProperty(required = false,value = "收货人电话")
	private String consigneeTelephone;
    /**
     * 交易创建时间
     */
	@ApiModelProperty(required = false,value = "交易创建时间")
	private Long createTime;
	/**
	 * 配送方式（快递 自提）
	 */
	@ApiModelProperty(value = "配送方式（快递 自提）")
	private String shipMethod;
    /**
     * 订单状态
     */
	@ApiModelProperty(required = false,value = "订单状态")
	private String tradeStatus;
	/**
	 * 是否被删除 0 未删除 1删除
	 */
	@ApiModelProperty(required = false,value = "是否被删除 0 未删除 1删除")
	private Integer isDel;

	@ApiModelProperty(value = "优惠券集合")
	private List<EsCouponDTO> couponList;

	@ApiModelProperty(value = "订单集合")
	private List<EsOrderDTO> orderList;

	@ApiModelProperty(value = "赠品列表" )
	private List<EsFullDiscountGiftVO> giftList;

	@ApiModelProperty(value = "赠送优惠券列表")
	private List<EsCouponVO> giftCouponList;

	@ApiModelProperty(value = "赠送积分")
	private Integer giftPoint;

	/**
	 * 取消订单时间
	 */
	@ApiModelProperty(value = "取消订单时间")
	private Long cancelTime;


}
