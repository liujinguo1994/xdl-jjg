package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员商品收藏
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberCollectionGoodsDO implements Serializable {


    /**
     * 主键ID
     */
    private Long id;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 收藏商品ID
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private Double goodsPrice;
    /**
     * 商品现价
     */
    @TableField(exist = false)
    private Double currentPrice;
    /**
     * 商品编号
     */
    private String goodsSn;
    /**
     * 商品图片
     */
    private String goodsImg;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 剩余库存
     */
    @TableField(exist = false)
    private Integer quantity;
    /**
     * 商品分类id
     */
    private Long categoryId;
    /**
     * 商品降价提醒 1不提醒，2提醒
     */
    private Integer priceRemind;
    /**
     * 库存紧缺
     */
    private String shortQuantity;
    /**
     * 降价金额
     */
    private Double cutMoney;
    /**
     * skuId
     */
    private Long skuId;
    /**
     *  2失效
     */
    private Integer loseSign;

}
