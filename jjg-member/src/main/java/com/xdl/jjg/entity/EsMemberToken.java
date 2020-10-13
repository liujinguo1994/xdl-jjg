package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员token信息表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-10 14:47:35
 */
@Data
@TableName("es_member_token")
public class EsMemberToken extends Model<EsMemberToken> {

	private static final long serialVersionUID = 1L;


	/**
	 * 会员ID
	 */
	@TableField("member_id")
	private Long memberId;

	/**
	 * token
	 */
	@TableField("token")
	private String token;

	/**
	 * 过期时间
	 */
	@TableField("expire_time")
	private Long expireTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;

}
