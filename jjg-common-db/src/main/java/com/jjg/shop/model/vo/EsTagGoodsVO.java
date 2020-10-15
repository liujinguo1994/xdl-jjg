package com.jjg.shop.model.vo;

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
public class EsTagGoodsVO implements Serializable {

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
