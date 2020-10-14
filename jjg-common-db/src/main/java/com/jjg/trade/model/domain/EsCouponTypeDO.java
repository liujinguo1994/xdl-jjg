package com.jjg.member.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-21 10:38:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsCouponTypeDO implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;

	private String couponName;

	private String couponCode;
	private Integer isGoods;

}
