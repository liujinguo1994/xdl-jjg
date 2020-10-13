package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_promotion_goods")
public class EsPromotionGoods extends Model<EsPromotionGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * SKU id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
    /**
     * 活动id
     */
    @TableField("activity_id")
    private Long activityId;
    /**
     * 活动类型
     */
    @TableField("promotion_type")
    private String promotionType;
    /**
     * 活动标题
     */
    private String title;
    /**
     * 参与活动的商品数量
     */
    private Integer num;
    /**
     * 活动时商品的价格
     */
    private Double price;
    /**
     * 商家ID
     */
    @TableField("shop_id")
    private Long shopId;

    /**
     * 活动开始时间
     */
    @TableField("start_time")
    private Long startTime;

    /**
     * 活动结束时间
     */
    @TableField("end_time")
    private Long endTime;
    /**
     * 是否下架 0.没有下架，1 下架
     */
    @TableField("is_lower_shelf")
    @TableLogic(value="0",delval="1")
    private Integer isLowerShelf;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
