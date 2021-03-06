package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCommentCategoryClassifyVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 0商品
	 */
	@ApiModelProperty(required = false, value = "商品标签")
	private List<EsCommentCategoryVO> goodLabels;
	/**
	 * 1物流
	 */
	@ApiModelProperty(required = false, value = "物流标签")
	private List<EsCommentCategoryVO> expressLabels;
	/**
	 * 2服务
	 */
	@ApiModelProperty(required = false, value = "服务标签")
	private List<EsCommentCategoryVO> serviceLabels;
}
