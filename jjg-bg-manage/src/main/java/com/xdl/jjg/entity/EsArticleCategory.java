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
 * 文章分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@TableName("es_article_category")
public class EsArticleCategory extends Model<EsArticleCategory> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 分类名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 父分类id
	 */
	@TableField("parent_id")
	private Long parentId;

	/**
	 * 路径
	 */
	@TableField("path")
	private String path;

	/**
	 * 是否允许删除
	 */
	@TableField("allow_delete")
	private Integer allowDelete;

	/**
	 * 分类类型
	 */
	@TableField("type")
	private String type;

	/**
	 * 排序
	 */
	@TableField("sort")
	private Integer sort;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
