package com.jjg.member.model.vo.wap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xdl.jjg.model.domain.EsComrImglDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 移动端-投诉信息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsWapComplaintVO implements Serializable {
	private static final long serialVersionUID = 3869181426623518657L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 投诉内容
	 */
	@ApiModelProperty(value = "投诉内容")
	private String content;

	/**
	 * 投诉原因Id
	 */
	@ApiModelProperty(value = "投诉原因Id")
	private Long reasonId;

	/**
	 * 投诉原因
	 */
	@ApiModelProperty(value = "投诉原因")
	private String reasonName;

	/**
	 * 投诉类型Id
	 */
	@ApiModelProperty(value = "投诉类型Id")
	private Long typeId;

	/**
	 * 投诉类型
	 */
	@ApiModelProperty(value = "投诉类型")
	private String typeName;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

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
	 * 处理状态
	 */
	@ApiModelProperty(value = "处理状态")
	private String state;

	/**
	 * 联系方式
	 */
	@ApiModelProperty(value = "联系方式")
	private String phone;

	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

	/**
	 * 投诉图片
	 */
	@ApiModelProperty(value = "投诉图片")
	private List<EsComrImglDO> comrImglDOList;

	/**
	 * 订单商品集合
	 */
	@ApiModelProperty(value = "订单商品集合")
	private List<EsWapComplaintOrderItemsVO> itemsVOList;

}
