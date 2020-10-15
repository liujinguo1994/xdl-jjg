package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-08 11:06:57
 */
@Data
public class EsGrowthWeightConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 类型 0:Recency,1:Frequency,2Monetary
     */
	private Integer type;

    /**
     * 权重
     */
	private Double weight;

    /**
     * 状态
     */
	private Boolean state;

}
