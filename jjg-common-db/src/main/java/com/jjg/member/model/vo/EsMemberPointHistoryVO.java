package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员积分明细VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:27
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberPointHistoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "会员ID")
	private Long memberId;

    /**
     * 积分值
     */
	@ApiModelProperty(value = "积分值")
	private Integer gradePoint;

    /**
     * 操作时间
     */
	@ApiModelProperty(value = "操作时间")
	private Long createTime;

    /**
     * 操作理由0：其他送 1：购物送 2：评论送
     */
	@ApiModelProperty(value = "操作理由")
	private Integer reason;

    /**
     * 积分类型
     */
	@ApiModelProperty(value = "积分类型")
	private Integer gradePointType;

	/**
	 * 积分类型Str
	 */
	private Integer gradePointTypeStr;

    /**
     * 操作者
     */
	@ApiModelProperty(value = "操作者")
	private String operator;

	/**
	 * 当前积分
	 */
	@ApiModelProperty(value = "当前积分")
	private Integer currentPoint;

	/**
	 * 详细说明
	 */
	@ApiModelProperty(value = "详细说明")
	private String description;

	/**
	 * 购物赠送数量
	 */
	@ApiModelProperty(value = "购物赠送数量")
	private String shoppingNum;

	/**
	 * 评论赠送数量
	 */
	@ApiModelProperty(value = "评论赠送数量")
	private String commentNum;

	/**
	 * 其他赠送数量
	 */
	@ApiModelProperty(value = "其他赠送数量")
	private String otherNum;

	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

	protected Serializable pkVal() {
		return this.id;
	}

}
