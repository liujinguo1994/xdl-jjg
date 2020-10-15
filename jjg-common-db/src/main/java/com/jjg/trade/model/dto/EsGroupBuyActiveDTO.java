package com.jjg.trade.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class EsGroupBuyActiveDTO implements Serializable {

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
	@JsonSerialize(using = ToStringSerializer.class)
	private Long startTime;

    /**
     * 团购结束时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long endTime;

    /**
     * 团购报名截止时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long joinEndTime;

    /**
     * 团购添加时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long addTime;

    /**
     * 团购活动标签Id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long actTagId;

    /**
     * 参与团购商品数量
     */
	private Integer goodsNum;

	protected Serializable pkVal() {
		return this.id;
	}

}
