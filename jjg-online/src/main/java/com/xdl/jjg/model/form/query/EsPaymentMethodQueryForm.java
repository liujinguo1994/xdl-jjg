package com.xdl.jjg.model.form.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsPaymentMethodQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付方式名称
     */
	@ApiModelProperty(value = "支付方式名称")
	private String methodName;

    /**
     * 支付插件名称
     */
	@ApiModelProperty(value = "支付插件名称")
	private String pluginId;

    /**
     * pc是否可用
     */
	@ApiModelProperty(value = "pc是否可用")
	private String pcConfig;

    /**
     * wap是否可用
     */
	@ApiModelProperty(value = "wap是否可用")
	private String wapConfig;

    /**
     * app 原生是否可用
     */
	@ApiModelProperty(value = "app 原生是否可用")
	private String appNativeConfig;

    /**
     * 支付方式图片
     */
	@ApiModelProperty(value = "支付方式图片")
	private String image;

    /**
     * 是否支持原路退回
     */
	@ApiModelProperty(value = "是否支持原路退回")
	private Integer isRetrace;

    /**
     * app RN是否可用
     */
	@ApiModelProperty(value = "app RN是否可用")
	private String appReactConfig;

}
