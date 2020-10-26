package com.jjg.trade.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 待领取优惠券
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-04-20
 */
@Data
@ApiModel
public class EsWaitGetCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 待领取数量
	 */
	@ApiModelProperty(value = "待领取数量")
	private Integer count;

	/**
	 * 待领取优惠券集合
	 */
	@ApiModelProperty(value = "待领取优惠券集合")
	private List<EsCouponVO> couponVOList;

}
