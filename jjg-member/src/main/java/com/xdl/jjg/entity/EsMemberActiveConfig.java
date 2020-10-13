package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员活跃度配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-02 19:50:01
 */
@Data
@TableName("es_member_active_config")
public class EsMemberActiveConfig extends Model<EsMemberActiveConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 天数
	 */
	@TableField("days")
	private Integer days;

	/**
	 * 订单数
	 */
	@TableField("orders")
	private Integer orders;

	/**
	 * 会员类型 0新增，1活跃，2休眠,3普通
	 */
	@TableField("member_type")
	private Integer memberType;

	/**
	 * 店铺ID
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 未访问天数
	 */
	@TableField("visit_days")
	private Integer visitDays;

}
