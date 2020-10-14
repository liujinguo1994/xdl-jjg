package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 *我的资产
 * @author xl 236154186@qq.com
 * @since 2019-12-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMyMeansDO implements Serializable {

    /**
     * 账户余额
     */
    private Double balanceAccount;
    /**
     * 优惠券数量
     */
    private Integer couponNum;
    /**
     * 积分数量
     */
    private Long pointNum;
    /**
     * 收藏商品数量
     */
    private Integer collectionGoodNum;
    /**
     * 收藏店铺数量
     */
    private Integer collectionShopNum;

}
