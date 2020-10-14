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
public class EsGoodsParamsVO implements Serializable {
    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 商品id
     */

	@ApiModelProperty(value = "商品id")
	private Long goodsId;

    /**
     * 参数id
     */

	@ApiModelProperty(value = "参数id")
	private Long paramId;

    /**
     * 参数名字
     */
	@ApiModelProperty(value = "参数名字")
	private String paramName;

    /**
     * 参数值
     */
	@ApiModelProperty(value = "参数值")
	private String paramValue;
}
