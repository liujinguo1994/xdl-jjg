package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-10 16:43:10
 */
@Data
@Accessors(chain = true)
@TableName("es_goods_user_token")
public class EsGoodsUserToken extends Model<EsGoodsUserToken> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId("user_id")
	private Long userId;

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

	@Override
	protected Serializable pkVal() {
		return this.userId;
	}
}
