package com.xdl.jjg.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-07-31 11:07:50
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsComplaintDTO implements Serializable {

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
	 * 投诉原因
	 */
	private Long reasonId;

	/**
	 * 子订单订单编号
	 */
	@TableField("order_sn")
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
	private Long typeId;

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
	 * 处理内容
	 */
	private String dealContent;

	/**
	 * 是否撤销 0不撤销 1撤销
	 */
	private Integer isDel;

	private List<EsComrImglDTO> comrImglDTOList;

}
