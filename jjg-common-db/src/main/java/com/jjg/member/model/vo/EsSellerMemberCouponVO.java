package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 卖家后台会员优惠券
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSellerMemberCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @ApiModelProperty(required = false,value = "会员id",example = "1")
    private Long memberId;
    /**
     * 用户名
     */
    @ApiModelProperty(required = false,value = "用户名")
    private String name;
    /**
     * 手机号
     */
    @ApiModelProperty(required = false,value = "手机号")
    private String mobile;
    /**
     * 优惠券表主键 (es_coupon)
     */
    @ApiModelProperty(required = false,value = "优惠券id")
	private Long couponId;
    /**
     * 使用时间
     */
    @ApiModelProperty(required = false,value = "使用时间",example = "123456")
	private Long updateTime;
    /**
     * 领取时间
     */
    @ApiModelProperty(required = false,value = "领取时间",example = "13456")
	private Long createTime;
    /**
     * 订单编号
     */
    @ApiModelProperty(required = false,value = "订单编号")
	private String orderSn;
    /**
     * 使用状态
     */
    @ApiModelProperty(required = false,value = "使用状态(1:正常，2:已使用，3失效)",example = "1")
	private Integer state;


}
