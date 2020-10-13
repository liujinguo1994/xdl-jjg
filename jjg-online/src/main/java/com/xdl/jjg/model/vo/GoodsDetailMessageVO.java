package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品详情页面返回参数VO
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel(description = "商品详情页面返回参数VO ")
public class GoodsDetailMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "是否收藏")
	private Boolean isCollection;

	@ApiModelProperty(value = "商品印象")
	List<Map<String, Object>> labelsGroup;

	@ApiModelProperty(value = "好评率")
	private Double goodCommentRate;



}
