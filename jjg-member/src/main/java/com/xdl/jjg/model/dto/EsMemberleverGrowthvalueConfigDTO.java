package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员等级成长值配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-18 15:10:59
 */
@Data
@ToString
public class EsMemberleverGrowthvalueConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 等级名称
     */
	private String levelName;

    /**
     * 成长值下线
     */
	private Integer growthValue;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 是否删除
     */
	private Integer isDel;


}
