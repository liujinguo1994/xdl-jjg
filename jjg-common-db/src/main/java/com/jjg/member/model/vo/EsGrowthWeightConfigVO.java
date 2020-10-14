package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * VO rfm权重
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-08 11:06:58
 */
@Data
@ApiModel
public class EsGrowthWeightConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@ApiModelProperty(required = false, value = "主键ID", example = "1")
	private Long id;

    /**
     * 类型 0:RFM策略,1:评价模型策略,2收藏模型策略
     */
	@ApiModelProperty(required = false,value = "类型 0:RFM策略,1:评价模型策略,2收藏模型策略",example = "1")
	private Integer type;

    /**
     * 权重
     */
	@ApiModelProperty(required = false,value = "权重",example = "0.5")
	private Double weight;

    /**
     * 状态
     */
	@ApiModelProperty(required = false,value = "状态",example = "1")
	private Boolean state;


}
