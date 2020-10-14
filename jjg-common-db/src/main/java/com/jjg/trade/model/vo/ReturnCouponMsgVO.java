package com.jjg.member.model.vo;

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
public class ReturnCouponMsgVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠金额
     */
	@ApiModelProperty(value = "优惠金额")
	private Double totalPrice;


    /**
     * 使用数量
     */
	@ApiModelProperty(value = "每人限领数量")
	private Integer num;
	public ReturnCouponMsgVO(){
		this.setTotalPrice(0.0);
		this.setNum(0);
	}


}
