package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

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
@TableName("es_admin_tag_goods")
public class EsAdminTagGoods extends Model<EsAdminTagGoods> {

	private static final long serialVersionUID = 1L;

	/**
	 * 标签id
	 */
	@TableField("tag_id")
	private Long tagId;

	/**
	 * 商品id
	 */
	@TableField("goods_id")
	private Long goodsId;

}
