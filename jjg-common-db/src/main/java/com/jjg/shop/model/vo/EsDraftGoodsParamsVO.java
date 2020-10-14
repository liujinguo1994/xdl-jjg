package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsDraftGoodsParamsVO implements Serializable {

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 草稿ID
     */
	@ApiModelProperty(value = "草稿ID")
	private Long draftGoodsId;

    /**
     * 参数ID
     */

	@ApiModelProperty(value = "参数ID")
	private Long paramId;

    /**
     * 参数名
     */
	@ApiModelProperty(value = "参数名")
	private String paramName;

    /**
     * 参数值
     */
	@ApiModelProperty(value = "参数值")
	private String paramValue;


}
