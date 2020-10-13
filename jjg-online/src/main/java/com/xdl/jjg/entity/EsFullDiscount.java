package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 满减满赠-es_full_discount
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_full_discount")
public class EsFullDiscount extends Model<EsFullDiscount> {

	private static final long serialVersionUID = 1L;

	/**
	 * 活动id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 优惠门槛金额
	 */
	@TableField("full_money")
	private Double fullMoney;

	/**
	 * 减现金
	 */
	@TableField("minus_value")
	private Double minusValue;

	/**
	 * 活动是否减现金
	 */
	@TableField("is_full_minus")
	private Integer isFullMinus;

	/**
	 * 是否免邮
	 */
	@TableField("is_free_ship")
	private Integer isFreeShip;

	/**
	 * 是否有赠品
	 */
	@TableField("is_send_gift")
	private Integer isSendGift;

	/**
	 * 是否赠优惠券
	 */
	@TableField("is_send_bonus")
	private Integer isSendBonus;

	/**
	 * 赠品id(支持多赠品)
	 */
	@TableField("gift_id")
	private Long giftId;

	/**
	 * 优惠券id
	 */
	@TableField("bonus_id")
	private Long bonusId;

	/**
	 * 是否打折
	 */
	@TableField("is_discount")
	private Integer isDiscount;

	/**
	 * 折扣
	 */
	@TableField("discount_value")
	private Double discountValue;

	/**
	 * 活动开始时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	/**
	 * 活动结束时间
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
	 * 商品参与方式，全部商品：1，部分商品：2
	 */
	@TableField("range_type")
	private Integer rangeType;

	/**
	 * 是否停用
	 */
	@TableLogic(value="0",delval="1")
	@TableField("is_del")
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
	 * 商家名称
	 */
	@TableField("shop_name")
	private String shopName;

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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
