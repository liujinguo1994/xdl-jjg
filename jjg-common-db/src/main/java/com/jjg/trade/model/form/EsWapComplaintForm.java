package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 移动端-投诉信息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Data
@ApiModel
public class EsWapComplaintForm implements Serializable {

	private static final long serialVersionUID = 5101287271156760430L;

	/**
     * 卖家ID
     */
	@ApiModelProperty(value = "卖家ID")
	@NotNull(message = "卖家ID不能为空")
	private Long shopId;

	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	@NotBlank(message = "订单编号不能为空")
	private String orderSn;

	/**
	 * 投诉类型ID
	 */
	@ApiModelProperty(value = "投诉类型ID")
	@NotNull(message = "投诉类型ID不能为空")
	private Long typeId;

	/**
	 * 投诉原因ID
	 */
	@ApiModelProperty(value = "投诉原因ID")
	@NotNull(message = "投诉原因ID不能为空")
	private Long reasonId;

    /**
     * 投诉内容
     */
	@ApiModelProperty(value = "投诉内容")
	private String content;

	/**
	 * 图片路径拼接字符串
	 */
	@ApiModelProperty(value = "图片路径拼接字符串")
	private String imgStr;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	@NotBlank(message = "手机号必填")
	private String phone;

}
