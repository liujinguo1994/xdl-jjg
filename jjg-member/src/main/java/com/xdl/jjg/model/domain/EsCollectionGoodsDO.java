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
public class EsCollectionGoodsDO implements Serializable {

    /**
     * 商品信息列表
     */
    List<EsMemberCollectionGoodsDO> esMemberCollectionGoodsDOList;
    /**
     * 失效数量
     */
    Integer effect  = 0;
    /**
     * 降价数量
     */
    Integer price = 0;
    /**
     * 分类数量
     */
    List<EsCollectCateryNumDO> esCollectCateryNumDOList;

}
