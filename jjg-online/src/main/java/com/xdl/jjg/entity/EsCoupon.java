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
 * 优惠卷
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_coupon")
public class EsCoupon extends Model<EsCoupon> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 优惠券名称
	 */
	@TableField("title")
	private String title;

	/**
	 * 优惠券面额
	 */
	@TableField("coupon_money")
	private Double couponMoney;

	/**
	 * 优惠券门槛价格
	 */
	@TableField("coupon_threshold_price")
	private Double couponThresholdPrice;

	/**
	 * 使用起始时间
	 */
	@TableField("start_time")
	private Long startTime;

	/**
	 * 使用截止时间
	 */
	@TableField("end_time")
	private Long endTime;

	/**
	 * 发行量
	 */
	@TableField("create_num")
	private Integer createNum;

	/**
	 * 每人限领数量
	 */
	@TableField("limit_num")
	private Integer limitNum;

	/**
	 * 已被使用的数量
	 */
	@TableField("used_num")
	private Integer usedNum = 0;

	/**
	 * 店铺ID
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 已被领取的数量
	 */
	@TableField("received_num")
	private Integer receivedNum = 0;

	/**
	 * 店铺名称
	 */
	@TableField("seller_name")
	private String sellerName;

	/**
	 * 1正常，2下架
	 */
	@TableField("is_del")
	private Integer isDel;

	/**
	 * 是否允许领取 1是 2 否
	 */
	@TableField("is_receive")
	private Integer isReceive;

	/**
	 * 有效天数
	 */
	@TableField("valid_day")
	private Integer validDay;

	/**
	 * 优惠券类型
	 */
	@TableField("coupon_type")
	private String couponType;

	/**
	 * 是否为赠券
	 */
	@TableField("is_coupons")
	private Integer isCoupons;
	/**
	 * 是否支持促销商品
	 */
	@TableField("is_pro_goods")
	private Integer isProGoods;

	@TableField("is_goods")
	private Integer isGoods;

	@TableField("is_register")
	private Integer isRegister;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
