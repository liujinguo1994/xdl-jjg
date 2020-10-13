package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 自提时间
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_self_time")
public class EsSelfTime extends Model<EsSelfTime> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 开始时间
	 */
	@TableField(value = "start_time", fill = FieldFill.INSERT)
	private Long startTime;
	/**
	 * 结束时间
	 */
	@TableField(value = "end_time", fill = FieldFill.INSERT_UPDATE)
	private Long endTime;
	/**
	 * 人数
	 */
	@TableField(value = "person_number")
	private Integer personNumber;

	/**
	 * 当前自提人数
	 */
	@TableField(value = "current_person")
	private Integer currentPerson;

	/**
	 * 自提日期ID
	 */
	@TableField("date_id")
	private Long dateId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
