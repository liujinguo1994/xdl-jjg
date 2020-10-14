package com.xdl.jjg.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * RFM表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsMemberRfmConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 消费间隔
     */
	private Integer recency;

    /**
     * 消费间隔成长值
     */
	private Integer recencyGrowthValue;

    /**
     * 消费频率
     */
	private Integer frequency;

    /**
     * 消费频率成长值
     */
	private Integer frequencyGrowthValue;

    /**
     * 消费金额
     */
	private Double monetary;

    /**
     * 消费金额成长值
     */
	private Integer monetaryGrowthValue;

    /**
     * rfm信息json字符串
     */
    private String rfmInfo;

}
