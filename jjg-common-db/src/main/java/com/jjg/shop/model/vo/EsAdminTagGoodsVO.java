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
 * @since 2019-07-27 14:57:57
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsAdminTagGoodsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
	@ApiModelProperty(value = "标签id")
	private Long tagId;

    /**
     * 商品id
     */
	@ApiModelProperty(value = "商品id")
	private Long goodsId;



}
