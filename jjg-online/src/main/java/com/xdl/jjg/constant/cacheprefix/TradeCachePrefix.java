package com.xdl.jjg.constant.cacheprefix;

/**
 * @ClassName: cacheprefix
 * @Description: 缓存前缀
 * @Author: libw  981087977@qq.com
 * @Date: 6/3/2019 19:53
 * @Version: 1.0
 */
public enum TradeCachePrefix {

    /* ================促销================= */
    /**
     * 促销活动
     */
    PROMOTION_KEY,

    /*** 单品立减 */
    STORE_ID_MINUS_KEY,

    /*** 第二件半价 */
    STORE_ID_HALF_PRICE_KEY,

    /*** 满优惠 */
    STORE_ID_FULL_DISCOUNT_KEY,
    /** 商品打折key  */
    GOODS_DISCOUNT_KEY,

    /**
     * 限时抢购活动缓存key前缀
     */
    STORE_ID_SECKILL_KEY,

    /**
     * 团购活动缓存key前缀
     */
    STORE_ID_GROUP_BUY_KEY,

    /**
     * 积分商品缓存key前缀
     */
    STORE_ID_EXCHANGE_KEY,


    /* ================交易================= */
    /**
     * 交易_会员购物车
     */
    CART_MEMBER_ID_KEY,

    /**
     * 交易_交易价格的前缀
     */
    PRICE_SESSION_ID_KEY,

    /**
     * 交易_交易单
     */
    TRADE_SESSION_ID_KEY,

    /**
     * 结算参数
     */
    CHECKOUT_PARAM_ID_KEY,

    /**
     * 交易单号前缀
     */
    TRADE_SN_CACHE_KEY,

    /**
     * 订单编号前缀
     */
    ORDER_SN_CACHE_KEY,

    /**
     * 订单编号前缀
     */
    PAY_LOG_SN_CACHE_KEY,

    /**
     * 交易
     */
    TRADE_KEY;

    public String getPrefix() {
        return this.name() + "_";
    }
}
