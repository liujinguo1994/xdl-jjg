package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * <p>
 * 会员商品收藏
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_collection_goods")
public class EsMemberCollectionGoods extends Model<EsMemberCollectionGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员ID
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 收藏商品ID
     */
    @TableField("goods_id")
	private Long goodsId;
    /**
     * 收藏商品时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 商品名称
     */
    @TableField("goods_name")
	private String goodsName;
    /**
     * 商品价格
     */
    @TableField("goods_price")
	private Double goodsPrice;
    /**
     * 商品编号
     */
    @TableField("goods_sn")
	private String goodsSn;
    /**
     * 商品图片
     */
    @TableField("goods_img")
	private String goodsImg;
    /**
     * 店铺id
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 商品分类id
     */
    @TableField("category_id")
    private Long categoryId;
    /**
     * 商品降价提醒
     */
    @TableField("price_remind")
    private Integer priceRemind;
    /**
     * skuId
     */
    @TableField("sku_id")
    private Long skuId;



}
