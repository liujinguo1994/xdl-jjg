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
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_category")
public class EsCategory extends Model<EsCategory> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 分类名称
     */
	private String name;
    /**
     * 分类父id
     */

    @TableField("parent_id")
	private Long parentId;
    /**
     * 分类父子路径
     */
    @TableField("category_path")
	private String categoryPath;
    /**
     * 该分类下商品数量
     */
    @TableField("goods_count")
	private Integer goodsCount;
    /**
     * 分类排序
     */
    @TableField("category_order")
	private Integer categoryOrder;
    /**
     * 分类图标
     */
	private String image;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
