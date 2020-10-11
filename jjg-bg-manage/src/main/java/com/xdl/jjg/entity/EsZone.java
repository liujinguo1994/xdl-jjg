package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 专区管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Data
@TableName("es_zone")
public class EsZone extends Model<EsZone> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 专区名称
	 */
	@TableField("zone_name")
	private String zoneName;

	/**
	 * 标题
	 */
	@TableField("title")
	private String title;

	/**
	 * 副标题
	 */
	@TableField("subtitle")
	private String subtitle;

	/**
	 * 图片1
	 */
	@TableField("picture1")
	private String picture1;

	/**
	 * 图片2
	 */
	@TableField("picture2")
	private String picture2;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
