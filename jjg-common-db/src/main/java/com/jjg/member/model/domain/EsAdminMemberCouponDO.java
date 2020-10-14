package com.jjg.member.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 后台会员优惠券列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsAdminMemberCouponDO implements Serializable {

    /**
     * 会员id
     */
    private Long id;
    /**
     * 名称
     */
	private String name;
    /**
     * 手机号
     */
	private String mobile;
    /**
     * 待领取数量
     */
	/*private Integer unclaimed;*/
    /**
     * 已领取待使用数量
     */
	private Integer noUseNum;
    /**
     * 已使用数量
     */
	private Integer hasMadeNum;
    /**
     * 已过期数量
     */
	private Integer expireCouponNum;

}
