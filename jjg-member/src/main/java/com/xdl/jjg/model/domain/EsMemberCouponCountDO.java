package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员优惠券
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
public class EsMemberCouponCountDO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 会员ID
     */
	private Long memberId;

    /**
     * 优惠券表主键 (es_coupon)
     */
	private Long couponId;

    /**
     * 优惠券使用次数
     */
    private Integer num;
}
