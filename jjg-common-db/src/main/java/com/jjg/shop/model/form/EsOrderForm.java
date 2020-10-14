package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单明细表-es_orderQueryForm
 * </p>
 *
 * @author WANGAF 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
public class EsOrderForm extends QueryPageForm {

    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "取消原因")
	private String cancelReason;
	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "收货人姓名")
	private String shipName;
	/**
	 * 收货地址
	 */
	@ApiModelProperty(value = "收货地址")
	private String shipAddr;
	/**
	 * 收货人手机
	 */
	@ApiModelProperty(value = "收货人手机")
	private String shipMobile;

	@ApiModelProperty(value = "订单备注")
	private String remark;

	/**
	 * 配送地区-省份ID
	 */
	@ApiModelProperty(value = "省份ID")
	private Long shipProvinceId;
	/**
	 * 配送地区-城市ID
	 */
	@ApiModelProperty(value = "城市ID")
	private Long shipCityId;
	/**
	 * 配送地区-区(县)ID
	 */
	@ApiModelProperty(value = "区(县)ID")
	private Long shipCountyId;
	/**
	 * 配送街道id
	 */
	@ApiModelProperty(value = "街道id")
	private Long shipTownId;
	/**
	 * 配送地区-省份
	 */
	@ApiModelProperty(value = "省份")
	private String shipProvince;
	/**
	 * 配送地区-城市
	 */
	@ApiModelProperty(value = "城市")
	private String shipCity;
	/**
	 * 配送地区-区(县)
	 */
	@ApiModelProperty(value = "区(县)")
	private String shipCounty;
	/**
	 * 配送街道
	 */
	@ApiModelProperty(value = "街道")
	private String shipTown;

	/**
	 * 订单状态
	 */
	@ApiModelProperty(value = "订单状态")
	private String orderState;
}
