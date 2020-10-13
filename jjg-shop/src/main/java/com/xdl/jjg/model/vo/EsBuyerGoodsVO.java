package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@Api
public class EsBuyerGoodsVO implements  Serializable {

	@ApiModelProperty(value = "分类ID")
	private Long categoryId;
	@ApiModelProperty("分类名称")
	private String categoryName;

	private List<EsGoodsVO> goodsList;

}
