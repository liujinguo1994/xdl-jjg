package com.jjg.trade.model.domain;

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
public class EsPromotionGoodsDO extends Model<EsPromotionGoodsDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * SKU id
     */
    private Long skuId;

    /**
     * 活动开始时间
     */
    private Long createTime;

    /**
     * 活动结束时间
     */
    private Long updateTime;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动类型
     */
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
    private Long shopId;

    /**
     * 公司标识符
     */
    private String companyCode;

    /**
     * 活动开始时间
     */
    private Long startTime;

    /**
     * 活动结束时间
     */
    private Long endTime;

    /**
     * 是否下架 0.没有下架，1 下架
     */
    private Integer isLowerShelf;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
