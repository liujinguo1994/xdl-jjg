package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 举报
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-16 09:28:26
 */
@Data
@ApiModel
public class EsWapReportForm implements Serializable {


	private static final long serialVersionUID = -7875793293882375734L;
	/**
	 * 举报内容
	 */
	@ApiModelProperty(value = "举报内容")
	private String complaintReason;

	/**
	 * 图片路径拼接字符串
	 */
	@ApiModelProperty(value = "图片路径拼接字符串")
	private String imgStr;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "店铺ID")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(value = "店铺名称")
	private String shopName;

	/**
	 * 联系方式
	 */
	@ApiModelProperty(value = "联系方式")
	private String phone;

}
