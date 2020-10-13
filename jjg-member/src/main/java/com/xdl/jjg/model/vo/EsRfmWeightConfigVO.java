package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-07 15:50:06
 */
@Data
@ApiModel
public class EsRfmWeightConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@ApiModelProperty(value = "主键id")
	private Long id;

    /**
     * 类型 0:Recency,1:Frequency,2Monetary
     */
	@ApiModelProperty(value = "类型 0:Recency,1:Frequency,2Monetary")
	private Integer type;

    /**
     * 权重
     */
	@ApiModelProperty(value = "权重")
	private Double weight;

    /**
     * 状态0:正常，1删除
     */
	@ApiModelProperty(value = "状态0:正常，1删除")
	private Integer state;


}
