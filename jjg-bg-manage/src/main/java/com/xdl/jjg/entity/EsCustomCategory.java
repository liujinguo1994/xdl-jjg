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
 * 自定义分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Data
@TableName("es_custom_category")
public class EsCustomCategory extends Model<EsCustomCategory> {
	private static final long serialVersionUID = -1577279185118145553L;
	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 自定义分类名称
	 */
	@TableField("category_name")
	private String categoryName;

	/**
	 * 所属专区
	 */
	@TableField("zone_id")
	private Long zoneId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
