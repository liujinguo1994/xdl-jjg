package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


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
public class EsOpenidMobileVO implements Serializable {

private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 用户唯一标识
	 */
	@ApiModelProperty(value = "用户唯一标识")
	private String openid;

	/**
	 * 手机号码
	 */
	@ApiModelProperty(value = "手机号码")
	private String mobile;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

	/**
	 * 最后修改时间
	 */
			
	@ApiModelProperty(value = "最后修改时间")
	private Long updateTime;

	}
