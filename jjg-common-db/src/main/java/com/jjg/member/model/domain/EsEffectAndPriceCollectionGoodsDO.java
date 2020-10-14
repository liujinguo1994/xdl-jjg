package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 会员商品收藏
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsEffectAndPriceCollectionGoodsDO implements Serializable {


    /**
     * 商品收藏
     */
    List<EsMemberCollectionGoodsDO> esMemberCollectionGoodsDOList;
    /**
     * 失效商品id
     */
    List<Long> effectGooodIds;
    /**
     * 降价商品id
     */
    List<Long> cutPriceIds;


}
