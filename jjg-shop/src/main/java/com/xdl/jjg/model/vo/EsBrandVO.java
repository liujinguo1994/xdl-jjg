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
 * @author WAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsBrandVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 品牌名称
     */
	@ApiModelProperty(value = "品牌名称")
	private String name;

    /**
     * 品牌图标
     */
	@ApiModelProperty(value = "品牌图标")
	private String logo;

    /**
     * 是否删除，0删除1未删除
     */
	@ApiModelProperty(value = "是否删除，0删除1未删除")
	private Integer isDel;

	protected Serializable pkVal() {
		return this.id;
	}

}
