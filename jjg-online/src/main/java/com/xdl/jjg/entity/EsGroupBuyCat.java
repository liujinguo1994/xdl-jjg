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
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_group_buy_cat")
public class EsGroupBuyCat extends Model<EsGroupBuyCat> {

	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 父类id
	 */
	@TableField("parent_id")
	private Long parentId;

	/**
	 * 分类名称
	 */
	@TableField("cat_name")
	private String catName;

	/**
	 * 分类结构目录
	 */
	@TableField("cat_path")
	private String catPath;

	/**
	 * 分类排序
	 */
	@TableField("cat_order")
	private Integer catOrder;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
