package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author LBW 981087977@qq.com
 * @since 2019-11-21 10:38:45
 */
@Data
@Accessors(chain = true)
@TableName("es_coupon_type")
public class EsCouponType extends Model<EsCouponType> {

	private static final long serialVersionUID = 1L;

		private Double id;

	@TableField("coupon_name")
	private String couponName;

	@TableField("coupon_code")
	private String couponCode;

	@TableField("is_goods")
	private Integer isGoods;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
