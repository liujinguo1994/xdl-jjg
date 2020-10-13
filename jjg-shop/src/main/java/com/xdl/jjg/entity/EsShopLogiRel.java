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
 * @author WAF 82698865@qq.com
 * @since 2019-07-17 14:59:13
 */
@Data
@Accessors(chain = true)
@TableName("es_shop_logi_rel")
public class EsShopLogiRel extends Model<EsShopLogiRel> {

	private static final long serialVersionUID = 1L;

	@TableField("logi_id")
	private Long logiId;

	@TableField("shop_id")
	private Long shopId;

}
