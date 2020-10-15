package com.jjg.member.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.member.model.domain.EsComrImglDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 举报表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-31 11:07:50
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsComplaintVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(required = false,value = "主键id")
	private Long id;

	/**
	 * 投诉内容
	 */
	@ApiModelProperty(required = false,value = "投诉内容")
	private String content;

	/**
	 * 投诉原因Id
	 */
	@ApiModelProperty(required = false,value = "投诉原因Id")
	private Long reasonId;

	/**
	 * 投诉原因
	 */
	@ApiModelProperty(required = false,value = "投诉原因")
	private String reasonName;

	/**
	 * 子订单订单编号
	 */
	@TableField("order_sn")
	@ApiModelProperty(required = false,value = "子订单订单编号")
	private String orderSn;

	/**
	 * 订单详情
	 */
	@ApiModelProperty(required = false,value = "订单详情")
	private String orderItems;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(required = false,value = "店铺ID")
	private Long shopId;



	/**
	 * 投诉类型Id
	 */
	@ApiModelProperty(required = false,value = "投诉类型Id")
	private Long typeId;

	/**
	 * 投诉类型
	 */
	@ApiModelProperty(required = false,value = "投诉类型")
	private String typeName;

	/**
	 * 处理状态
	 */
	@ApiModelProperty(required = false,value = "处理状态")
	private String state;

	/**
	 * 联系方式
	 */
	@ApiModelProperty(required = false,value = "联系方式")
	private String phone;

	/**
	 * 会员id
	 */
	@ApiModelProperty(required = false,value = "会员id")
	private Long memberId;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(required = false,value = "会员名称")
	private String memberName;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(required = false,value = "创建时间")
	private Long createTime;

	/**
	 * 处理内容
	 */
	@ApiModelProperty(required = false,value = "处理内容")
	private String dealContent;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(required = false,value = "店铺名称")
	private String shopName;


	private List<EsComrImglDO> comrImglDOList;

}
