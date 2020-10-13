package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.shopx.trade.api.model.domain.dto.EsPromotionGoodsDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@TableName("es_half_price")
public class EsHalfPrice extends Model<EsHalfPrice> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 起始时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	/**
	 * 结束时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	/**
	 * 活动标题
	 */
	@TableField("title")
	private String title;

	/**
	 * 商品参与方式1全部商品：2，部分商品
	 */
	@TableField("range_type")
	private Integer rangeType;

	/**
	 * 是否停用 0.没有停用 1.停用
	 */
	@TableField("is_del")
	@TableLogic(value="0",delval="1")
	private Integer isDel;

	/**
	 * 活动说明
	 */
	@TableField("description")
	private String description;

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
	 * 定时任务Id
	 */
	@TableField("job_id")
	private Long jobId;

	/**
	 * 商品集合
	 */
	@TableField(exist = false)
	private List<EsPromotionGoodsDTO> goodsList;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
