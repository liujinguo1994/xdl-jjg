package com.xdl.jjg.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 投诉信息表--es_complaintQueryForm
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:29
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsComplaintQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 买家ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "买家ID")
	private Long memberId;

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
     * 子订单编号
     */
	@ApiModelProperty(value = "子订单编号")
	private String orderItems;

    /**
     * 创建时间
     */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

    /**
     * 投诉类型
     */
	@ApiModelProperty(value = "投诉类型")
	private String type;

    /**
     * 回复内容
     */
	@ApiModelProperty(value = "回复内容")
	private String reContent;

	@ApiModelProperty(value = "")
	private String memberName;

	@ApiModelProperty(value = "")
	private String phone;

	@ApiModelProperty(value = "")
	private Integer isDel;

    /**
     * 处理状态
     */
	@ApiModelProperty(value = "处理状态")
	private String state;

    /**
     * 投诉原因
     */
	@ApiModelProperty(value = "投诉原因")
	private String reason;

    /**
     * 处理内容
     */
	@ApiModelProperty(value = "处理内容")
	private String dealContent;

}
