package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 评分权重
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-29 14:18:13
 */
@Data
@ToString
public class EsGradeWeightConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 评分名称
     */
	private String commentName;

    /**
     * 评分类型 0:商品评分,1物流评分,2服务评分
     */
	private Integer commentType;

    /**
     * 评分权重值
     */
	private Double weightValue;


}
