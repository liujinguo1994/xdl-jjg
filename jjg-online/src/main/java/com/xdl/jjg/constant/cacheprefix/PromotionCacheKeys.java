package com.xdl.jjg.constant.cacheprefix;

import com.xdl.jjg.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 促销活动缓存
 *
 * @author libw
 * @version v1.0
 * @Date: 6/18/2019 09：25
 */
public class PromotionCacheKeys {

    /**
     * 读取满优惠redis key
     *
     * @param activityId
     * @return
     */
    public static final String getFullDiscountKey(Long activityId) {
        String key = TradeCachePrefix.STORE_ID_FULL_DISCOUNT_KEY.getPrefix() + activityId;
        return key;
    }

    /**
     * 读取第二件半价活动redis key
     *
     * @param activityId
     * @return
     */
    public static final String getHalfPriceKey(Long activityId) {
        String key = TradeCachePrefix.STORE_ID_HALF_PRICE_KEY.getPrefix() + activityId;
        return key;
    }

    /**
     * 读取单品立减活动 redis key
     *
     * @param activityId
     * @return
     */
    public static final String getMinusKey(Long activityId) {
        String key = TradeCachePrefix.STORE_ID_MINUS_KEY.getPrefix() + activityId;
        return key;
    }

    /**
     * 读取限时抢购 redis key
     *
     * @param time 格式为(年月日):20171215   如果为空则默认查询当天
     * @return
     */
    public static final String getSeckillKey(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        if (StringUtil.isEmpty(time)) {
            time = dateFormat.format(new Date());
        }
        String key = TradeCachePrefix.STORE_ID_SECKILL_KEY.getPrefix() + time;
        return key;
    }

    /**
     * 读取折扣活动 redis key
     *
     * @param activityId
     * @return
     */
    public static final String getGoodsDiscount(Long activityId) {
        String key = TradeCachePrefix.GOODS_DISCOUNT_KEY.getPrefix() + activityId;
        return key;
    }


}
