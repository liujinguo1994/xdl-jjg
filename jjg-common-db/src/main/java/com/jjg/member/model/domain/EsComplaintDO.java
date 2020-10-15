package com.jjg.member.model.domain;

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
public class EsComplaintDO implements Serializable {

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
	 * 订单详情
	 */
	private Long goodId;


	/**
	 * 店铺ID
	 */
	private Long shopId;



	/**
	 * 投诉类型ID
	 */
	private Long typeId;

	/**
	 * 投诉原因ID
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
	 * 收货人姓名
	 */
	private String shipName;

	/**
	 * 投诉类型
	 */
	private String typeName;

	/**
	 * 投诉原因
	 */
	private String reasonName;

	/**
	 * 店铺名称
	 */
	private String shopName;

}
