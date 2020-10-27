package com.xdl.jjg.manager;


import com.jjg.trade.model.dto.EsPromotionGoodsDTO;
import com.jjg.trade.model.enums.PromotionTypeEnum;

import java.util.List;

/**
 * 活动规则检测
 *
 * @author Snow create in 2018/4/25
 * @version v2.0
 * @since v7.0.0
 */
public interface PromotionRuleManager {

    /**
     * 检测活动与活动之间的规则冲突
     *
     * @param goodsDTOList 活动商品
     * @param typeEnum     活动类型枚举
     * @param startTime    活动起始时间
     * @param endTime      活动截止时间
     * @param activityId   活动ID，如果是添加活动，此值传null
     */
    void verifyRule(List<EsPromotionGoodsDTO> goodsDTOList, PromotionTypeEnum typeEnum, Long startTime,
                    Long endTime, Long activityId);

    /**
     * 校验活动时间
     * @author: libw 981087977@qq.com
     * @date: 2019/07/31 15:43:14
     * @param startTime     活动开始时间
     * @param endTime       活动结束时间
     * @param typeEnum      活动枚举
     * @param activityId    活动id
     * @param shopId        店铺id
     * @return: void
     */
    void verifyTime(long startTime, long endTime, PromotionTypeEnum typeEnum, Long activityId, Long shopId);
}
