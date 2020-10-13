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
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-07-27 14:57:56
 */
@Data
@Accessors(chain = true)
@TableName("es_admin_tags")
public class EsAdminTags extends Model<EsAdminTags> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 标签名字
	 */
	@TableField("tag_name")
	private String tagName;

	/**
	 * 关键字
	 */
	@TableField("mark")
	private String mark;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
