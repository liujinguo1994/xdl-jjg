package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-18 09:39:17
 */
@Data
@TableName("es_shop_promise")
public class EsShopPromise extends Model<EsShopPromise> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 卖家id
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 承诺内容
	 */
	@TableField("content")
	private String content;

	/**
	 * 有效状态
	 */
	@TableField("state")
	private Integer state = 0;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
