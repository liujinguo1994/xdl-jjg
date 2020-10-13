package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsGroupBuyActiveDO extends Model<EsGroupBuyActiveDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 活动Id
     */
	private Long id;

    /**
     * 活动名称
     */
	private String actName;

    /**
     * 团购开启时间
     */
	private Long startTime;

    /**
     * 团购结束时间
     */
	private Long endTime;

    /**
     * 团购报名截止时间
     */
	private Long joinEndTime;

    /**
     * 团购添加时间
     */
	private Long addTime;

    /**
     * 团购活动标签Id
     */
	private Long actTagId;

    /**
     * 参与团购商品数量
     */
	private Integer goodsNum;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
