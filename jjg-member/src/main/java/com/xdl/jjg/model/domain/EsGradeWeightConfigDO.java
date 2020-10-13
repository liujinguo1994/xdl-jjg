package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 评分配置权重
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-29 14:18:12
 */
@Data
public class EsGradeWeightConfigDO implements Serializable {

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
