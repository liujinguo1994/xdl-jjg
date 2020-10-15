package com.jjg.shop.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-07-17 14:59:14
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsShopLogiRelVO implements  Serializable {

    private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键ID")
	private Long id;
	/**
	 * 物流公司名称
	 */
	@ApiModelProperty(value = "物流公司名称")
	private String name;
	/**
	 * 物流公司code
	 */
	@ApiModelProperty(value = "物流公司code")
	private String code;

	@ApiModelProperty(value = "店铺ID 空则未开启 有值则已开启")
	private Long shopId;

}
