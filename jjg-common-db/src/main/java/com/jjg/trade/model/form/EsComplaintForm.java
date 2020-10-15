package com.jjg.trade.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 投诉信息表--es_complaintForm
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:28
 */
@Data
@Api
public class EsComplaintForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 卖家ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "卖家ID")
	private Long shopId;

    /**
     * 投诉内容
     */
	@ApiModelProperty(value = "投诉内容")
	private String content;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 投诉类型
     */
	@ApiModelProperty(value = "投诉类型")
	private String type;

	/**
	 * 联系方式
	 */
	@ApiModelProperty(value = "联系电话")
	private String phone;

    /**
     * 回复内容
     */
	@ApiModelProperty(value = "回复内容")
	private String reContent;

    /**
     * 投诉原因ID
     */
	@ApiModelProperty(value = "投诉原因ID")
	private Long reasonId;

    /**
     * 处理内容
     */
	@ApiModelProperty(value = "处理内容")
	private String dealContent;

	/**
	 * 图片路径拼接字符串
	 */
	@ApiModelProperty(value = "图片路径拼接字符串")
	private String imgStr;

}
