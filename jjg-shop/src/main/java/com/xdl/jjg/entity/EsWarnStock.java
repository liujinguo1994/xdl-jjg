package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("es_warn_stock")
public class EsWarnStock extends Model<EsWarnStock> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品分类ID
     */
    @TableField("category_id")
	private Long categoryId;
    /**
     * 预警库存
     */
    @TableField("warn_quantity")
	private Integer warnQuantity;
    /**
     * 店铺ID
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
