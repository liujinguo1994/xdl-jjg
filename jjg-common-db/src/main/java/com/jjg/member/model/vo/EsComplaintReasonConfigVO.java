package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 投诉原因类型配置VO
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 09:56:28
 */
@Data
@ApiModel
public class EsComplaintReasonConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@ApiModelProperty(required = false,value = "主键id",example = "1")
	private Long id;

    /**
     * 投诉原因
     */
	@ApiModelProperty(required = false,value = "投诉原因")
	private String complaintReason;

    /**
     * 投诉类型id
     */
	@ApiModelProperty(required = false,value = "投诉类型id",example = "1")
	private Long complainTypeId;

	/**
	 * 投诉类型名称
	 */
	@ApiModelProperty(required = false,value = "投诉类型名称")
	private String complaintTypeName;

    /**
     * 创建时间
     */
	@ApiModelProperty(required = false,value = "创建时间",example = "12345")
	private Long createTime;


}
