package com.jjg.trade.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 公司运费模版详情VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-28 10:41:09
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsShipCompanyDetailsVO implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "")
	private String areaId;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "")
	private Long shopId;

    /**
     * 1：非生鲜 2：生鲜
     */
	@ApiModelProperty(value = "1：非生鲜 2：生鲜")
	private Integer type;

	@ApiModelProperty(value = "")
	private Double lowerPrice;

	@ApiModelProperty(value = "")
	private Double highPrice;

	@ApiModelProperty(value = "")
	private Double firstCompany;

	@ApiModelProperty(value = "")
	private Double firstPrice;

	@ApiModelProperty(value = "")
	private Double continuedCompany;

	@ApiModelProperty(value = "")
	private Double continuedPrice;
	private Long isNg;
	protected Serializable pkVal() {
		return this.id;
	}

}
