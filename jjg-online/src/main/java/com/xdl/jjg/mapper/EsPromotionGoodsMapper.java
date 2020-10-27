package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsPromotionGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsPromotionGoodsMapper extends BaseMapper<EsPromotionGoods> {

    /**
     * 查询是否有冲突的活动
     * @author: libw 981087977@qq.com
     * @date: 2019/08/01 17:17:23
     * @param goodsIdStr        商品id集合
     * @param promotionTypes    活动类型集合
     * @param startTime         活动开始时间
     * @param endTime           活动结束时间
     * @param activityId        活动id
     * @return: java.util.List<com.shopx.trade.dao.entity.EsPromotionGoods>
     */
    List<EsPromotionGoods> verifyConflict(@Param("goodsIdStr") String goodsIdStr, @Param("promotionTypes") String promotionTypes,
                                          @Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("activityId") Long activityId);

    List<EsPromotionGoods>  getPromotionByGoodsId(@Param("goodsIdList") List<Long> goodsIdList, @Param("currentTime") Long currentTime);

    List<EsPromotionGoods>  getPromotionBySkuId(@Param("skuIdList") List<Long> skuIdList, @Param("currentTime") Long currentTime);

    int getIsBeOverdueByActivityId(@Param("activityId") Long activityId, @Param("timeMillis") Long timeMillis);

}
