package com.jjg.trade.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-21 10:38:46
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsCouponTypeVO implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "优惠券类型名称")
	private String couponName;

	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "优惠券类型code")
	private String couponCode;
	@ApiModelProperty(value = "是否选中商品 1 是 2 否")
	private Integer isGoods;
	protected Serializable pkVal() {
		return this.id;
	}

}
