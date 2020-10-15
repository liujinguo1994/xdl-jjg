package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 订单主表-es_tradeQueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsTradeQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 买家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "买家id")
	private Long memberId;

    /**
     * 买家用户名
     */
	@ApiModelProperty(value = "买家用户名")
	private String memberName;

    /**
     * 支付方式id
     */
	@JsonSerialize(using = ToStringSerializer.class)
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
	@ApiModelProperty(value = "支付插件名称")
	private String pluginId;

    /**
     * 总价格
     */
	@ApiModelProperty(value = "总价格")
	private Double totalMoney;

    /**
     * 商品价格
     */
	@ApiModelProperty(value = "商品价格")
	private Double goodsMoney;

    /**
     * 运费
     */
	@ApiModelProperty(value = "运费")
	private Double freightMoney;

    /**
     * 优惠的金额
     */
	@ApiModelProperty(value = "优惠的金额")
	private Double discountMoney;

    /**
     * 收货地址id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "收货地址id")
	private Long consigneeId;

    /**
     * 收货人姓名
     */
	@ApiModelProperty(value = "收货人姓名")
	private String consigneeName;

    /**
     * 收货国家
     */
	@ApiModelProperty(value = "收货国家")
	private String consigneeCountry;

    /**
     * 收货国家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "收货国家id")
	private Long consigneeCountryId;

    /**
     * 收货省
     */
	@ApiModelProperty(value = "收货省")
	private String consigneeProvince;

    /**
     * 收货省id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "收货省id")
	private Long consigneeProvinceId;

    /**
     * 收货市
     */
	@ApiModelProperty(value = "收货市")
	private String consigneeCity;

    /**
     * 收货市id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "收货市id")
	private Long consigneeCityId;

    /**
     * 收货区
     */
	@ApiModelProperty(value = "收货区")
	private String consigneeCounty;

    /**
     * 收货区id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "收货区id")
	private Long consigneeCountyId;

    /**
     * 收货镇
     */
	@ApiModelProperty(value = "收货镇")
	private String consigneeTown;

    /**
     * 收货镇id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "收货镇id")
	private Long consigneeTownId;

    /**
     * 收货详细地址
     */
	@ApiModelProperty(value = "收货详细地址")
	private String consigneeAddress;

    /**
     * 收货人手机号
     */
	@ApiModelProperty(value = "收货人手机号")
	private String consigneeMobile;

    /**
     * 收货人电话
     */
	@ApiModelProperty(value = "收货人电话")
	private String consigneeTelephone;

    /**
     * 交易创建时间
     */
	@ApiModelProperty(value = "交易创建时间")
	private Long createTime;

    /**
     * 订单状态
     */
	@ApiModelProperty(value = "订单状态")
	private String tradeStatus;

    /**
     * 是否删除
     */
	@ApiModelProperty(value = "是否删除")
	private Integer isDel;

}
