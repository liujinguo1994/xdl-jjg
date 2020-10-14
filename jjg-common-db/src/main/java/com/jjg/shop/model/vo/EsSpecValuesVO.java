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
public class EsSpecValuesVO implements Serializable {
    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 规格项id
     */

	@ApiModelProperty(value = "规格项id")
	private Long specId;

    /**
     * 规格值名字
     */
	@ApiModelProperty(value = "规格值名字")
	private String specValue;

    /**
     * 所属卖家
     */
	@ApiModelProperty(value = "所属卖家")
	private Long shopId;

    /**
     * 规格名称
     */
	@ApiModelProperty(value = "规格名称")
	private String specName;
}
