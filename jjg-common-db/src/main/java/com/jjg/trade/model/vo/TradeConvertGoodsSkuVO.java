package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.goods.api.model.domain.vo.EsSpecValuesVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 转化后的货品vo
 * @author Snow create in 2018/3/20
 * @version v2.0
 * @since v7.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeConvertGoodsSkuVO implements Serializable {


    @ApiModelProperty(value = "可用库存")
    private Integer enableQuantity;

    @ApiModelProperty(value = "货号")
    private Integer skuId;

    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品编号")
    private String sn;

    @ApiModelProperty(value = "库存")
    private Integer quantity;

    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "规格信息json")
    private String specs;

    @ApiModelProperty(value="成本价格")
    private Double cost;

    @ApiModelProperty(value="重量")
    private Double weight;

    @ApiModelProperty(value="分类id")
    private Integer categoryId;

    @ApiModelProperty(value="缩略图")
    private String thumbnail;

    @ApiModelProperty(value="卖家名称")
    private String sellerName;

    @ApiModelProperty(value="卖家id")
    private Integer sellerId;

    @ApiModelProperty(value="商品价格")
    private String goodsPrice;

    @ApiModelProperty(value="规格列表")
    private List<EsSpecValuesVO> specList;

    @ApiModelProperty(value = "谁承担运费0：买家承担，1：卖家承担")
    private Integer goodsTransfeeCharge;

    @ApiModelProperty(value = "上架状态 1上架  0下架")
    private Integer marketEnable;

    @ApiModelProperty(value = "是否被删除0 删除 1未删除")
    private Integer disabled;

}
