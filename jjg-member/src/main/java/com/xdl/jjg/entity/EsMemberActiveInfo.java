package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员活跃信息表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 17:14:43
 */
@Data
@TableName("es_member_active_info")
public class EsMemberActiveInfo extends Model<EsMemberActiveInfo> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 子订单订单编号
	 */
	@TableField("order_sn")
	private String orderSn;

	/**
	 * 店铺ID
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@TableField("shop_name")
	private String shopName;

	/**
	 * 会员ID
	 */
	@TableField("member_id")
	private Long memberId;

	/**
	 * 支付时间
	 */
	@TableField("payment_time")
	private Long paymentTime;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

}
