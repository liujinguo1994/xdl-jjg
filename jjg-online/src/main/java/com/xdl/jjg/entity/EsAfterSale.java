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
 * 售后维护配置
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_after_sale")
public class EsAfterSale extends Model<EsAfterSale> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键 ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 退货时间
	 */
	@TableField("return_goods_time")
	private Integer returnGoodsTime;

	/**
	 * 换货时间
	 */
	@TableField("change_goods_time")
	private Integer changeGoodsTime;

	/**
	 * 商品分类ID
	 */
	@TableField("category_id")
	private Long categoryId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
