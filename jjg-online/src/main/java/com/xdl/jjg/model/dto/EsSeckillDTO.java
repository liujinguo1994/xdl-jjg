package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsSeckillDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;
    /**
     * 活动名称
     */
	private String seckillName;
	/**
	 * 活动日期
	 */
	private Long startDay;
	/**
	 * 报名截至时间
	 */
	private Long applyEndTime;
    /**
     * 申请规则
     */
	private String seckillRule;
    /**
     * 商家id集合以逗号分隔
     */
	private String shopIds;
    /**
     * 状态（1编辑中2已发布3已结束）
     */
	private Integer state;
	/**
	 * 活动时刻列表
	 */
	private List<Integer> rangeList;

	/**
	 * 卖家端商家ID
	 */
	private Long shopId;


	protected Serializable pkVal() {
		return this.id;
	}

}
