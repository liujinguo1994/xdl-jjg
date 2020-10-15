package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSeckillDO extends Model<EsSeckillDO> {

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
	 * 卖家端报名状态
	 */
	private String applyStatus;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
