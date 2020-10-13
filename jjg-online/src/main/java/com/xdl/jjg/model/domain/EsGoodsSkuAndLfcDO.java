package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品sku实体
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-21 11:48:40
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class EsGoodsSkuAndLfcDO implements Serializable {

    private static final long serialVersionUID = 5102510694003249L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    private Long skuId;
    /**
     * 商品id
     */
    @ApiModelProperty(name = "goods_id", value = "商品id", hidden = true)
    private Long goodsId;
    /**
     * 商品名称
     */
    @ApiModelProperty(name = "goods_name", value = "商品名称", hidden = true)
    private String goodsName;
    /**
     * 商品编号
     */
    @ApiModelProperty(name = "sn", value = "商品编号", required = false)
    private String goodsSn;
    /**
     * 库存
     */
    @ApiModelProperty(name = "quantity", value = "库存", required = false)
    private Integer quantity;
    /**
     * 可用库存
     */
    @ApiModelProperty(name = "enable_quantity", value = "可用库存")
    private Integer enableQuantity;
    /**
     * 商品价格
     */
    @ApiModelProperty(name = "price", value = "商品价格", required = false)
    private Double morey;
    /**
     * 规格信息json
     */
    @ApiModelProperty(name = "specs", value = "规格信息json", hidden = true)
    @JsonIgnore
    private String specs;
    /**
     * 成本价格
     */
    @ApiModelProperty(name = "cost", value = "成本价格", required = true)
    private Double cost;
    /**
     * 重量
     */
    @ApiModelProperty(name = "weight", value = "重量", required = true)
    private Double weight;
    /**
     * 卖家id
     */
    @ApiModelProperty(name = "seller_id", value = "卖家id", hidden = true)
    private Long shopId;
    /**
     * 卖家名称
     */
    @ApiModelProperty(name = "seller_name", value = "卖家名称", hidden = true)
    private String shopName;
    /**
     * 分类id
     */
    @ApiModelProperty(name = "category_id", value = "分类id", hidden = true)
    private Long categoryId;
    /**
     * 缩略图
     */
    @ApiModelProperty(name = "thumbnail", value = "缩略图", hidden = true)
    private String thumbnail;

    /**
     * 购买个数
     */
    @ApiModelProperty(name = "thumbnail", value = "缩略图", hidden = true)
    private Integer saleCount;

    public EsGoodsSkuAndLfcDO() {
    }
}