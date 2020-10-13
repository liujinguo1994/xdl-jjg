package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

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
@ToString
public class EsMemberCollectionGoodsDTO implements Serializable {


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
     * 收藏商品时间
     */
	private Long createTime;
    /**
     * 商品名称
     */
	private String goodsName;
    /**
     * 商品价格
     */
	private Double goodsPrice;
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
     * 备注
     */
	private String mark;
    /**
     * 商品分类id（目前是三级分类id,最好是传二级分类id，可能话传递二级分类给我）
     */
    private Long categoryId;
    /**
     * 降价提醒 1不提醒，2提醒
     */
    private Integer priceRemind;
    /**
     * skuId
     */
    private Long skuId;



}
