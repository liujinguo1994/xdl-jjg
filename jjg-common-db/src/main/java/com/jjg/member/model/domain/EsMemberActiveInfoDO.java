package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员活跃信息表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 17:14:45
 */
@Data
public class EsMemberActiveInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 子订单订单编号
     */
	private String orderSn;

    /**
     * 店铺ID
     */
	private Long shopId;

    /**
     * 店铺名称
     */
	private String shopName;

    /**
     * 会员ID
     */
	private Long memberId;

    /**
     * 支付时间
     */
	private Long paymentTime;

    /**
     * 创建时间
     */
	private Long createTime;

}
