package com.xdl.jjg.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class EsSellerMemberCouponDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
	private Long memberId;
    /**
     * 用户名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 优惠券表主键 (es_coupon)
     */
	private Long couponId;
    /**
     * 使用时间
     */
	private Long updateTime;
    /**
     * 领取时间
     */
	private Long createTime;
    /**
     * 订单编号
     */
	private String orderSn;
    /**
     * 使用状态
     */
	private Integer state;


}
