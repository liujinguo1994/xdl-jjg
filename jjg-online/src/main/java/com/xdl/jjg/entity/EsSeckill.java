package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_seckill")
public class EsSeckill extends Model<EsSeckill> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 活动名称
	 */
	@TableField("seckill_name")
	private String seckillName;
	/**
	 * 活动日期
	 */
	@TableField(value = "start_day", fill = FieldFill.INSERT)
	private Long startDay;
	/**
	 * 报名截至时间
	 */
	@TableField(value = "apply_end_time", fill = FieldFill.INSERT_UPDATE)
	private Long applyEndTime;
	/**
	 * 申请规则
	 */
	@TableField("seckill_rule")
	private String seckillRule;
	/**
	 * 商家id集合以逗号分隔
	 */
	@TableField("shop_ids")
	private String shopIds;
	/**
	 * 状态（1编辑中2已发布3已结束4已下架）
	 */
	private Integer state;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
