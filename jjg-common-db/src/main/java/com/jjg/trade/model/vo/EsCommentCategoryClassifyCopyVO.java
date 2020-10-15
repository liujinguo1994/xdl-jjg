package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 提供给买家端 的评价信息列表
 * 我的订单 订单商品详情
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCommentCategoryClassifyCopyVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 0商品
	 */
	@ApiModelProperty(required = false, value = "商品标签")
	private List<EsCommentCategoryCopyVO> goodLabels;
	/**
	 * 1物流
	 */
	@ApiModelProperty(required = false, value = "物流标签")
	private List<EsCommentCategoryCopyVO> expressLabels;
	/**
	 * 2服务
	 */
	@ApiModelProperty(required = false, value = "服务标签")
	private List<EsCommentCategoryCopyVO> serviceLabels;
}
