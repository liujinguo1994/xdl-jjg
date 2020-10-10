package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
@Data
@Accessors(chain = true)
@TableName("es_admin_user_token")
public class EsAdminUserToken extends Model<EsAdminUserToken> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
		@JsonSerialize(using = ToStringSerializer.class)
	@TableId("user_id")
	private Double userId;

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
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	@Override
	protected Serializable pkVal() {
		return this.userId;
	}
}
