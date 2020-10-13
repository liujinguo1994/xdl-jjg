package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 成长值配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-08 11:06:57
 */
@Data
@ToString
public class EsGrowthWeightConfigDTO implements Serializable {

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
     * 状态0:正常，1删除
     */
	private Integer state;


}
