package com.jjg.member.model.domain;

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
public class EsShowCollectGoodsDO implements Serializable {

    /**
     * 收藏商品列表展示
     */
    private List<EsMemberCollectionGoodsDO> esMemberCollectionGoodsDOList;
    /**
     * 商品统计分类列表展示
     */
    private List<EsCollectCateryNumDO> esCollectCateryNumDOList;
    /**
     * 失效商品数量
     */
    private Integer effectNum;
    /**
     * 降价商品数量
     */
    private Integer cutPriceNum;
    /**
     * 总条数
     */
    private Long allTotal;
}
