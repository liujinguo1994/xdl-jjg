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
public class EsCategoryBrandVO implements Serializable {
    /**
     * 分类id
     */
	@ApiModelProperty(value = "分类id")
	private Long categoryId;

    /**
     * 品牌id
     */
	@ApiModelProperty(value = "品牌id")
	private Long brandId;


}
