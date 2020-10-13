package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 微信关联手机号
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-09
 */
@Data
@ApiModel
public class EsAppletUserInfoVO implements Serializable {

private static final long serialVersionUID = 1L;

	/**
	 * 微信绑定的手机号
	 */
	@ApiModelProperty(value = "微信绑定的手机号")
	private String phoneNumber;
	/**
	 * 微信关联的其他手机号码list
	 */
	@ApiModelProperty(value = "微信关联的其他手机号码list")
	private List<EsOpenidMobileVO> mobileVOList;

}
