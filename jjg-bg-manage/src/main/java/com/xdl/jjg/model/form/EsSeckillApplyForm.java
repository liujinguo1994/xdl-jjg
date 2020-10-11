package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 审核限时抢购商品
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSeckillApplyForm implements Serializable {


	private static final long serialVersionUID = 4785665812171456168L;
	/**
	 * 活动id
	 */
	@ApiModelProperty(required = true,value = "活动id",example = "1")
	@NotNull(message = "活动id不能为空")
	private Long seckillId;
	/**
	 * 商品id
	 */
	@ApiModelProperty(required = true,value = "商品id",example = "1")
	@NotNull(message = "商品id不能为空")
	private Long goodsId;;
	/**
	 * 驳回原因
	 */
	@ApiModelProperty(value = "驳回原因")
	private String failReason;
	/**
	 * 审核状态结果(0:通过,1:不通过)
	 */
	@ApiModelProperty(required = true,value = "审核状态结果(0:通过,1:不通过)",example = "1")
	@NotNull(message = "审核状态结果不能为空")
	private Integer status;

	private Long id;

}
