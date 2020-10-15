package com.jjg.trade.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 返回给结算页面自提信息
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsDeliveryMessageVO extends Model<EsDeliveryMessageVO> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自提点ID")
	private Long DeliveryId;


	@ApiModelProperty(value = "自提点日期ID")
	private Long DateId;


	@ApiModelProperty(value = "自提时间点ID")
	private Long TimeId;

	/**
	 * 自提点名称
	 */
	@ApiModelProperty(value = "自提点名称")
	private String deliveryName;

	/**
	 * 门店地址
	 */
	@ApiModelProperty(value = "门店地址")
	private String address;

	/**
	 * 自提日期
	 */
	@ApiModelProperty(value = "自提日期")
	private Long selfDate;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	private Long startTime;
	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	private Long endTime;

	@ApiModelProperty(value = "收货人姓名")
	private String receiverName;

	@ApiModelProperty(value = "收货人手机")
	private String receiverMobile;


}
