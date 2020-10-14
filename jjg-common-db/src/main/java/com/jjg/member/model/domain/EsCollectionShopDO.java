package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员店铺收藏
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCollectionShopDO implements Serializable {


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
    private Double money;
    /**
     * 商品编号
     */
    private String goodsSn;
    /**
     * 商品图片
     */
    private String goodsImg;
    /**
     * 可用库存=SKU之和
     */
    private Integer quantity;
    /**
     * 详情
     */
    private String intro;
    /**
     * 原图路径
     */
    private String original;
    /**
     * 销量
     */
    private Integer buyCount;

}
