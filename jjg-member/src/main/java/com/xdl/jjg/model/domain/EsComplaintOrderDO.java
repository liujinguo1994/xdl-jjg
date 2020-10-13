package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class EsComplaintOrderDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 投诉内容
	 */
	private String content;

	/**
	 * 子订单订单编号
	 */
	private String orderSn;

	/**
	 * 商品id
	 */
	private Long goodId;

	/**
	 * 店铺ID
	 */
	private Long shopId;

	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 投诉类型
	 */
	private String type;

	/**
	 * 投诉原因
	 */
	private Long reasonId;

	/**
	 * 处理状态
	 */
	private String state;

	/**
	 * 联系方式
	 */
	private String phone;

	/**
	 * 会员id
	 */
	private Long memberId;

	/**
	 * 会员名称
	 */
	private String memberName;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * 是否有效
	 */
	private Integer isDel;


	/**
	 * 处理内容
	 */
	private String dealContent;


	private List<EsComrImglDO> comrImglDOList;

	/**
	 * 支付金额
	 */
	private Double payMoney;

	/**
	 * 收货人姓名
	 */
	private String shipName;

	private List<EsComplaintBuyerOrderItemsDO> esBuyerOrderItemsDO;

}
