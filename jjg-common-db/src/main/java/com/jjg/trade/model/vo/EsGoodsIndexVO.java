package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.goods.api.model.domain.vo.EsSpecValuesVO;
import com.shopx.trade.api.model.domain.EsPromotionGoodsDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsIndexVO implements Serializable {
    /**商品id*/
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    /**商品名称*/
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**缩略图*/
    @ApiModelProperty(value = "缩略图")
    private String original;

    /**商品价格*/
    @ApiModelProperty(value = "商品价格")
    private Double money;

    /**购买数*/
    @ApiModelProperty(value = "购买数")
    private Integer buyCount;

    /**评论数*/
    @ApiModelProperty(value = "评论数")
    private Integer commentNum;

    /**
     * 商品好評率
     */
    @ApiModelProperty(value = "商品好評率")
    private Double grade;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Integer shopId;
    /**
     * 店铺名字
     */
    @ApiModelProperty(value = "店铺名字")
    private String shopName;

    private List<EsPromotionGoodsDO> esPromotionGoodsDOList;

    @ApiModelProperty(value = "商品规格集合")
    private List<EsSpecValuesVO> specList;


}