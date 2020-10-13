package com.xdl.jjg.manager.event;

import com.shopx.trade.api.model.domain.vo.CartVO;

/**
 * 促销插件接口
 *
 * @author libw
 * @date 2019年6月25日下午3:32:10
 */
public interface PromotionEvent {

    /**
     * 计算购物车价格
     *
     * @param cart 购物车信息
     * @return ApiResponse
     */
//    void onPriceProcess(CartVO cart);

    /**
     * 计算活动
     * Version_2
     * @param cart
     */
    void onPromotionPriceProcess(CartVO cart);
}
