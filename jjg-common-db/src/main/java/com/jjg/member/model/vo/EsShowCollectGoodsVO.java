package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品收藏展示
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsShowCollectGoodsVO implements Serializable {

    /**
     * 收藏商品列表展示
     */
    @ApiModelProperty(required = false, value = "收藏商品列表展示")
    private List<EsMemberCollectionGoodsVO> esMemberCollectionGoodsDOList;
    /**
     * 商品统计分类列表展示
     */
    @ApiModelProperty(required = false, value = "商品统计分类列表展示")
    private List<EsCollectCateryNumVO> esCollectCateryNumDOList;
    /**
     * 失效商品数量
     */
    @ApiModelProperty(required = false, value = "失效商品数量",example = "1")
    private Integer effectNum;
    /**
     * 降价商品数量
     */
    @ApiModelProperty(required = false,value ="降价商品数量",example = "1")
    private Integer cutPriceNum;
    /**
     * 总条数
     */
    @ApiModelProperty(required = false,value ="总条数",example = "1")
    private Long allTotal;
}
