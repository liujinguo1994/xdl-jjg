package com.jjg.trade.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Form
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:28
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMyFootprintForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;

    /**
     * 店铺ID
     */
	@ApiModelProperty(value = "店铺ID")
	private Long shopId;

    /**
     * 商品价格
     */
	@ApiModelProperty(value = "商品价格")
	private Double money;

    /**
     * 商品图片
     */
	@ApiModelProperty(value = "商品图片")
	private String img;
}
