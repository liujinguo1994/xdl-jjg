package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 评分权重
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-29 14:18:13
 */
@Data
@ApiModel
public class EsGradeWeightConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(required = false, value = "主键ID", example = "1")
	private Long id;

    /**
     * 评分名称
     */
	@ApiModelProperty(required = false,value = "评分名称")
	private String commentName;

    /**
     * 评分类型 0:商品评分,1物流评分,2服务评分
     */
	@ApiModelProperty(required = false,value = "评分类型 0:商品评分,1物流评分,2服务评分", example = "1")
	private Integer commentType;

    /**
     * 评分权重值
     */
	@ApiModelProperty(required = false,value = "评分权重值",example = "1")
	private Double weightValue;


}
