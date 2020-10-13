package com.xdl.jjg.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_my_footprint")
public class EsMyFootprint extends Model<EsMyFootprint> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品ID
     */

    @TableField("goods_id")
	private Long goodsId;
    /**
     * 访问时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 店铺ID
     */

    @TableField("shop_id")
	private Long shopId;
    /**
     * 商品价格
     */
	private BigDecimal money;
    /**
     * 商品图片
     */
	private String img;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
