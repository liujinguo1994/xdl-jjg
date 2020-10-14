package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺收藏
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsQueryCollectionShopDO implements Serializable {

    /**
     * 收藏商品ID
     */
    private Long goodsId;
    /**
     * 收藏id
     */
	private Long id;
    /**
     * 会员id
     */
	private Long memberId;
    /**
     * 店铺id
     */
	private Long shopId;
    /**
     * 店铺名称
     */
	private String shopName;
    /**
     * 店铺logo
     */
	private String logo;
    /**
     * 店铺备注
     */
    private String mark;
    /**
     * 收藏商品列表
     */
    private List<EsMemberCollectionShopLabelDO> collectionShopLabelDOList;

    /**
     * 热门商品列表
     */
    private List<EsMemberGoodsDO> hotGoodList;

    /**
     * 上新商品列表
     */
    private List<EsMemberGoodsDO> newGoodList;

}
