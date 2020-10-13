package com.xdl.jjg.model.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsPaymentMethodForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 支付方式名称
     */
	private String methodName;
    /**
     * 支付插件名称
     */
	private String pluginId;
    /**
     * pc是否可用
     */
	private String pcConfig;
    /**
     * wap是否可用
     */
	private String wapConfig;
    /**
     * app 原生是否可用
     */
	private String appNativeConfig;
    /**
     * 支付方式图片
     */
	private String image;
    /**
     * 是否支持原路退回
     */
	private Integer isRetrace;
    /**
     * app RN是否可用
     */
	private String appReactConfig;


	protected Serializable pkVal() {
		return this.id;
	}

}
