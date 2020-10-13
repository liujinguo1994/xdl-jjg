package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 交易模块优惠卷
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsTradeCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(required = false,value = "主键ID",example = "1")
	private Long id;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(required = false,value = "优惠券名称")
	private String title;

    /**
     * 优惠券面额
     */
    @ApiModelProperty(required = false,value = "优惠券面额",example = "12.3")
	private Double couponMoney;

    /**
     * 优惠券门槛价格
     */
    @ApiModelProperty(required = false,value = "优惠券门槛价格",example = "13.2")
	private Double couponThresholdPrice;

    /**
     * 使用起始时间
     */
    @ApiModelProperty(required = false,value = "使用起始时间",example = "123456")
	private Long startTime;

    /**
     * 使用截止时间
     */
    @ApiModelProperty(required = false,value = "使用截止时间",example = "123456")
	private Long endTime;

    /**
     * 发行量
     */
    @ApiModelProperty(required = false,value = "发行量",example = "1")
	private Integer createNum;

    /**
     * 每人限领数量
     */
    @ApiModelProperty(required = false,value = "每人限领数量",example = "1")
	private Integer limitNum;

    /**
     * 已被使用的数量
     */
    @ApiModelProperty(required = false,value = "已被使用的数量",example = "1")
	private Integer usedNum;

    /**
     * 店铺ID
     */
    @ApiModelProperty(required = false,value = "店铺ID",example = "1")
	private Long shopId;

    /**
     * 已被领取的数量
     */
    @ApiModelProperty(required = false,value = "已被领取的数量",example = "1")
	private Integer receivedNum;

    /**
     * 店铺名称
     */
    @ApiModelProperty(required = false,value = "店铺名称",example = "1")
	private String sellerName;

    /**
     * 0正常，1下架
     */
    @ApiModelProperty(required = false,value = "0正常，1下架",example = "1")
	private Integer isDel;

    /**
     * 是否允许领取
     */
    @ApiModelProperty(required = false,value = "是否允许领取",example = "1")
	private Integer isReceive;

    /**
     * 有效天数
     */
    @ApiModelProperty(required = false,value = "有效天数",example = "1")
	private Integer validDay;

	/**
	 * 店铺log图标
	 */
	@ApiModelProperty(required = false,value = "店铺log图标",example = "1")
	private String  shopLogo;

}
