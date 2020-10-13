package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("es_group_buy_active")
public class EsGroupBuyActive extends Model<EsGroupBuyActive> {

	private static final long serialVersionUID = 1L;

	/**
	 * 活动Id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 活动名称
	 */
	@TableField("act_name")
	private String actName;

	/**
	 * 团购开启时间
	 */
	@TableField("start_time")
	private Long startTime;

	/**
	 * 团购结束时间
	 */
	@TableField("end_time")
	private Long endTime;

	/**
	 * 团购报名截止时间
	 */
	@TableField("join_end_time")
	private Long joinEndTime;

	/**
	 * 团购添加时间
	 */
	@TableField("add_time")
	private Long addTime;

	/**
	 * 团购活动标签Id
	 */
	@TableField("act_tag_id")
	private Long actTagId;

	/**
	 * 参与团购商品数量
	 */
	@TableField("goods_num")
	private Integer goodsNum;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
