package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 会员优惠券VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 优惠券名称
     */
	@ApiModelProperty(value = "优惠券名称")
	private String title;

    /**
     * 优惠券面额
     */
	@ApiModelProperty(value = "优惠券面额")
	private Double couponMoney;

    /**
     * 优惠券门槛价格
     */
	@ApiModelProperty(value = "优惠券门槛价格")
	private Double couponThresholdPrice;

    /**
     * 使用起始时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "使用起始时间")
	private Long startTime;

    /**
     * 使用截止时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "使用截止时间")
	private Long endTime;

    /**
     * 发行量
     */
	@ApiModelProperty(value = "发行量")
	private Integer createNum;

    /**
     * 每人限领数量
     */
	@ApiModelProperty(value = "每人限领数量")
	private Integer limitNum;

    /**
     * 已被使用的数量
     */
	@ApiModelProperty(value = "已被使用的数量")
	private Integer usedNum;

    /**
     * 店铺ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺ID")
	private Long shopId;

    /**
     * 已被领取的数量
     */
	@ApiModelProperty(value = "已被领取的数量")
	private Integer receivedNum;

    /**
     * 店铺名称
     */
	@ApiModelProperty(value = "店铺名称")
	private String sellerName;

    /**
     * 0正常，1下架
     */
	@ApiModelProperty(value = "0正常，1下架")
	private Integer isDel;

    /**
     * 是否允许领取
     */
	@ApiModelProperty(value = "是否允许领取")
	private Integer isReceive;

    /**
     * 有效天数
     */
	@ApiModelProperty(value = "有效天数")
	private Integer validDay;

	/**
	 * 优惠券类型
	 */
	@ApiModelProperty(value = "优惠券类型 1通用券CURRENCY  2 单品劵SINGLE_PRODUCT  3 品类劵CATEGORY 4 运费劵FREIGHT ")
	private String couponType;

	/**
	 * 商城首页使用 是否已领取
	 */
	@ApiModelProperty(value = "商城首页使用 是否已领取, 1 已领取，2：未领取")
	private Integer isReceived;

	@ApiModelProperty(value = "是否为赠券 1 是 ,0或2 否")
	private Integer isCoupons;
	@ApiModelProperty(value = "优惠券状态")
	private  String couponStatus;

	@ApiModelProperty(value = "优惠券类型文本")
	private String couponTypeText;
	@ApiModelProperty(value = "是否支持促销商品 0是 1否")
	private Integer isProGoods;
	@ApiModelProperty(value = "是否选择商品 1是 2否")
	private Integer isGoods;
	@ApiModelProperty(value = "是否是注册赠券 1 是 2 不是")
	private Integer isRegister;
	protected Serializable pkVal() {
		return this.id;
	}

}
