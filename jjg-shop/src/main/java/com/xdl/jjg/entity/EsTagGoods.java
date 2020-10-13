package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_tag_goods")
public class EsTagGoods extends Model<EsTagGoods> {

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
