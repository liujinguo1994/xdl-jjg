package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 商品折扣活动表
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-28 14:26:01
 */
@Data
@Accessors(chain = true)
@TableName("es_goods_discount")
public class EsGoodsDiscount extends Model<EsGoodsDiscount> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

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
	 * 折扣
	 */
	@TableField("discount")
	private Double discount;

	/**
	 * 活动标题
	 */
	@TableField("title")
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
	@TableField("remark")
	private String remark;

	/**
	 * 商家id
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	/**
	 * 修改时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;
	/**
	 * 定时任务Id
	 */
	@TableField("job_id")
	private Long jobId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
