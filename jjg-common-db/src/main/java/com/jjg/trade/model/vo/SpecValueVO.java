package com.jjg.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jjg.shop.model.domain.EsSpecValuesDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 规格值实体
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月22日 上午9:17:33
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class SpecValueVO extends EsSpecValuesDO implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1426807099688672502L;
	/**
	 * 商品sku——id
	 */
	@ApiModelProperty(name="sku_id",hidden=true)
	private Integer skuId;
	/**
	 * 商品大图
	 */
	@ApiModelProperty(hidden=true)
	private String big;
	/**
	 * 商品小图
	 */
	@ApiModelProperty(hidden=true)
	private String small;
	/**
	 * 商品缩略图
	 */
	@ApiModelProperty(hidden=true)
	private String thumbnail;
	/**
	 * 商品极小图
	 */
	@ApiModelProperty(hidden=true)
	private String tiny;
	
	/**
	 * 规格类型，1图片  0 非图片
	 */
	@ApiModelProperty(name="spec_type",value="该规格是否有图片，1 有 0 没有")
	private Integer specType;
	/**
	 * 规格图片
	 */
	@ApiModelProperty(name="spec_image",value="规格的图片")
	private String specImage;

	public SpecValueVO() {
		
	}


}
