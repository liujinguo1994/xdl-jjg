package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-07-06
 */
@Data
@Accessors(chain = true)
@TableName("es_coupon_receive_check")
public class EsCouponReceiveCheck extends Model<EsCouponReceiveCheck> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 手机号
	 */
	@TableField("mobile")
	private String mobile;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time",fill = FieldFill.INSERT)
	private Long createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;

	/**
	 * 用户名
	 */
	@TableField("user_name")
	private String userName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
