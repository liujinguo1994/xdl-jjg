package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 评价和收藏成长值配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-20 15:51:02
 */
@Data
public class EsGrowthValueStrategyDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 成长值类型
     */
	private Integer growthType;

    /**
     * 成长值
     */
	private Integer growthValue;
    /**
     * 每天限制次数
     */
    private Integer limitNum;
    /**
     * 开关
     */
    private Boolean configSwitch;

}
