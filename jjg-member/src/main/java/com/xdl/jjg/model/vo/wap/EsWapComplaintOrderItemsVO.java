package com.xdl.jjg.model.vo.wap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 移动端-投诉订单商品
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsWapComplaintOrderItemsVO implements Serializable {

	private static final long serialVersionUID = 4644660432151916152L;
	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;
	/**
	 * 商品ID
	 */
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;
	/**
	 * 商品名称
	 */
	@ApiModelProperty( value = "商品名称")
	private String name;
	/**
	 * 图片
	 */
	@ApiModelProperty(value = "图片")
	private String image;


}
