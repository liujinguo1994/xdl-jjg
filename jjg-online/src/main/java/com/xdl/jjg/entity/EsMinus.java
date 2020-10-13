package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 单品立减表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_minus")
public class EsMinus extends Model<EsMinus> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 单品立减金额
	 */
	@TableField("single_reduction_value")
	private Double singleReductionValue;
	/**
	 * 起始时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
	/**
	 * 结束时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;
	/**
	 * 单品立减活动标题
	 */
	private String title;
	/**
	 * 商品参与方式（1，全部商品 2，部分商品）
	 */
	@TableField("range_type")
	private Integer rangeType;
	/**
	 * 是否停用
	 */
	@TableField("is_del")
	@TableLogic(value="0",delval="1")
	private Integer isDel;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 商家id
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 活动开始时间
	 */
	@TableField("start_time")
	private Long startTime;

	/**
	 * 活动结束时间
	 */
	@TableField("end_time")
	private Long endTime;

	/**
	 * 定时任务ID
	 */
	@TableField("job_id")
	private Long jobId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
