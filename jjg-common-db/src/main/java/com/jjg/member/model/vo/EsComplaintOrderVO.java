package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * @since 2019-07-26 11:07:50
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsComplaintOrderVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(required = false, value = "主键id")
	private Long id;

	/**
	 * 投诉内容
	 */
	@ApiModelProperty(required = false, value = "投诉内容")
	private String content;

	/**
	 * 子订单订单编号
	 */
	@ApiModelProperty(required = false, value = "子订单订单编号")
	private String orderSn;


	/**
	 * 店铺ID
	 */
	@ApiModelProperty(required = false, value = "店铺ID")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(required = false, value = "店铺名称")
	private String shopName;

	/**
	 * 投诉类型ID
	 */
	@ApiModelProperty(required = false, value = "投诉类型ID")
	private Long typeId;

	/**
	 * 投诉原因ID
	 */
	@ApiModelProperty(required = false, value = "投诉原因ID")
	private Long reasonId;

	/**
	 * 处理状态
	 */
	@ApiModelProperty(required = false, value = "处理状态")
	private String state;

	/**
	 * 联系方式
	 */
	@ApiModelProperty(required = false, value = "联系方式")
	private String phone;

	/**
	 * 会员id
	 */
	@ApiModelProperty(required = false, value = "会员id")
	private Long memberId;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(required = false, value = "会员名称")
	private String memberName;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(required = false, value = "创建时间")
	private Long createTime;

	/**
	 * 是否有效
	 */
	@ApiModelProperty(required = false, value = "是否有效 0有效，1无效")
	private Integer isDel;


	/**
	 * 处理内容
	 */
	@ApiModelProperty(required = false, value = "处理内容")
	private String dealContent;

	/**
	 * 支付金额
	 */
	@ApiModelProperty(required = false, value = "支付金额")
	private Double payMoney;

	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(required = false, value = "收货人姓名")
	private String shipName;

	/**
	 * 投诉类型
	 */
	@ApiModelProperty(required = false, value = "投诉类型")
	private String typeName;

	/**
	 * 投诉原因
	 */
	@ApiModelProperty(required = false, value = "投诉原因")
	private String reasonName;

	/**
	 * 商品订单
	 */
	@ApiModelProperty(required = false, value = "商品订单")
	private List<EsComplaintBuyerOrderItemsVO> esBuyerOrderItemsVO;

}
