package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 自提点信息维护
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
//@Accessors(chain = true)
//@TableName("es_delivery_service")
public class EsDeliveryMessage extends Model<EsDeliveryMessage> {

	private static final long serialVersionUID = 1L;

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
