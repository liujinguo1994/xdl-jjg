package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * <p>
 * 购物车项
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_commercel_items")
public class EsCommercelItems extends Model<EsCommercelItems> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 购物车ID
     */
    @TableField("cart_id")
	private Long cartId;
    /**
     * 商品类型（1，普通商品，2赠品）
     */
	private Integer type;
    /**
     * 商品ID
     */
    @TableField("product_id")
	private Long productId;
    /**
     * 商品SKUID
     */
    @TableField("sku_id")
	private Long skuId;
    /**
     * 商品价格
     */
	private Double price;
    /**
     * 商品数量
     */
	private Integer quantity;
    /**
     * 商品编号
     */
    @TableField("goods_sn")
	private String goodsSn;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 修改时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;
    /**
     * 店铺ID
     */
    @TableField("shop_id")
	private Long shopId;


}
