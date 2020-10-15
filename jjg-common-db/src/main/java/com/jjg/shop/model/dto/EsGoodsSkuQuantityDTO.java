package com.jjg.shop.model.dto;

import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.co.EsGoodsSkuCO;
import lombok.Data;

import java.io.Serializable;

@Data
public class EsGoodsSkuQuantityDTO implements Serializable {

    /**
     * 调整后的虚拟库存
     */
    private Integer xnQuantity;
    /**
     * 库存
     */
    private Integer quantity;
    /**
     * 可用库存
     */
    private Integer enableQuantity;

    private EsGoodsCO esGoodsCO;

    private EsGoodsSkuCO esGoodsSkuCO;

    //买家调用传递
    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * SKUID
     */
    private Long skuId;
    /**
     * 商品数量
     */
    private Integer goodsNumber;

    private String orderSn;

    private Long activityId;
}
