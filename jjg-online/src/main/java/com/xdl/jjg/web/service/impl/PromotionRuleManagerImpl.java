package com.xdl.jjg.web.service.impl;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.StringUtil;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.dto.EsPromotionGoodsDTO;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.dao.entity.*;
import com.shopx.trade.dao.mapper.*;
import com.shopx.trade.service.service.PromotionRuleManager;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 活动规则检测
 *
 * @author Snow create in 2018/4/25
 * @version v2.0
 * @since v7.0.0
 */
@Service
public class PromotionRuleManagerImpl implements PromotionRuleManager {

    @Autowired
    private EsHalfPriceMapper halfPriceMapper;

    @Autowired
    private EsMinusMapper minusMapper;

    @Autowired
    private EsFullDiscountMapper fullDiscountMapper;

    @Autowired
    private EsSeckillMapper seckillMapper;

    @Autowired
    private EsPromotionGoodsMapper promotionGoodsMapper;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsGoodsService goodsService;

    /**
     * 检测活动与活动之间的规则冲突
     *
     * @param goodsList 活动商品
     * @param typeEnum     活动类型枚举
     * @param startTime    活动起始时间
     * @param endTime      活动截止时间
     * @param activityId   活动ID，如果是添加活动，此值传null
     */
    @Override
    public void verifyRule(List<EsPromotionGoodsDTO> goodsList, PromotionTypeEnum typeEnum, Long startTime,
                           Long endTime, Long activityId) {

        if (goodsList == null || goodsList.isEmpty()) {
            throw new ArgumentException(TradeErrorCode.NO_MERCHANDISE.getErrorCode(),
                    TradeErrorCode.NO_MERCHANDISE.getErrorMsg());
        }
        List<Long> goodsIdList = goodsList.stream().map(EsPromotionGoodsDTO::getGoodsId).collect(Collectors.toList());

        if (!goodsIdList.contains(-1L)) {
            DubboResult result = goodsService.buyCheckGoods(goodsIdList.stream().toArray(Long[]::new));
            if (!result.isSuccess()) {
                throw new ArgumentException(result.getCode(), result.getMsg());
            } else if (StringUtil.equals((String) ((Map) result.getData()).get("result"), "false")) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "商品不在同一店铺中");
            }
        }

        // 验证冲突，参与了限时抢购的商品，只能参加限时抢购和满减
        switch (typeEnum) {
            case HALF_PRICE:
                this.verifyConflict(goodsList, startTime, endTime, PromotionTypeEnum.HALF_PRICE.name(), activityId);
                break;
            case MINUS:
                this.verifyConflict(goodsList, startTime, endTime, PromotionTypeEnum.MINUS.name(), activityId);
                break;
            case GOODS_DISCOUNT:
                this.verifyConflict(goodsList, startTime, endTime, PromotionTypeEnum.GOODS_DISCOUNT.name(), activityId);
                break;
            case SECKILL:
                this.verifyConflict(goodsList, startTime, endTime, PromotionTypeEnum.SECKILL.name(), activityId);
                break;
            default:
                break;
        }
    }

    /**
     * 判断冲突的活动
     *
     * @param goodsList     商品id列表
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param promotionType 活动类型
     * @param activityId    活动id
     * @author: libw 981087977@qq.com
     * @date: 2019/08/01 16:32:29
     * @return: void
     */
    private void verifyConflict(List<EsPromotionGoodsDTO> goodsList, Long startTime, Long endTime, String promotionType,
                                Long activityId) {
        List<Long> goodsIdList = goodsList.stream().map(EsPromotionGoodsDTO::getGoodsId).collect(Collectors.toList());
        String goodsIdStr = StringUtils.join(goodsIdList, ",");

        //如果商品参与的 团购活动 或者 限时抢购活动 或者 积分活动，则不能参与其他任何活动。
        List<String> promotionTypeList = new ArrayList<>();

        if (promotionType.equals(PromotionTypeEnum.MINUS.name())
                || promotionType.equals(PromotionTypeEnum.HALF_PRICE.name())
                || promotionType.equals(PromotionTypeEnum.GOODS_DISCOUNT.name())) {

            promotionTypeList.add("'" + PromotionTypeEnum.SECKILL.name() + "'");
        } else if (promotionType.equals(PromotionTypeEnum.SECKILL.name())) {
            promotionTypeList.add("'" + PromotionTypeEnum.MINUS.name() + "'");
            promotionTypeList.add("'" + PromotionTypeEnum.HALF_PRICE.name() + "'");
            promotionTypeList.add("'" + PromotionTypeEnum.GOODS_DISCOUNT.name() + "'");
        }

        String promotionTypes = StringUtils.join(promotionTypeList.toArray(), ",");

        /*
         * 冲突活动商品的条件
         * 1. 当前准备添加的活动商品在 es_promotion_goods 存在 并且 (-1也为存在)
         * 2. 同一类型  并且
         * 3. 当前准备添加的活动的 活动起始时间大于之前活动的起始时间  并且
         * 4. 当前准备添加的活动的 活动截止时间大于之前活动的截止时间
         */
        List<EsPromotionGoods> list = this.promotionGoodsMapper.verifyConflict(goodsIdStr, promotionTypes, startTime,
                endTime, activityId);
        //如果没有冲突商品，则返回继续执行
        if (list.isEmpty()) {
            return;
        }

        //冲突商品集合
        List<String> goodsNameList = new ArrayList<>();
        List<Long> goodsId = new ArrayList<>();
        for (EsPromotionGoods goodsDO : list) {
            for (EsPromotionGoodsDTO goodsDTO : goodsList) {
                if (goodsDO.getGoodsId().intValue() == goodsDTO.getGoodsId().intValue()) {

                    if (!goodsId.contains(goodsDO.getGoodsId())) {
                        goodsNameList.add(goodsDTO.getGoodsName());
                        goodsId.add(goodsDO.getGoodsId());
                    }

                }
            }
        }

        if (!goodsNameList.isEmpty()) {
            String goodsName = StringUtils.join(goodsNameList, ",");
            throw new ArgumentException(TradeErrorCode.CONFLICT_OF_ACTIVITY_COMMODITIES.getErrorCode(),
                    goodsName + TradeErrorCode.CONFLICT_OF_ACTIVITY_COMMODITIES.getErrorMsg());
        }
    }

    /**
     * 校验活动时间
     *
     * @param startTime  活动开始时间
     * @param endTime    活动结束时间
     * @param typeEnum   活动枚举
     * @param activityId 活动id
     * @param shopId     店铺id
     * @author: libw 981087977@qq.com
     * @date: 2019/07/31 15:43:14
     * @return: void
     */
    @Override
    public void verifyTime(long startTime, long endTime, PromotionTypeEnum typeEnum, Long activityId, Long shopId) {
        // 新增加得活动时间，不能在现有活动得开始时间和结束活动里
        int num = 0;
        switch (typeEnum) {
            case HALF_PRICE:
                EsHalfPrice halfPrice = new EsHalfPrice();
                halfPrice.setStartTime(startTime);
                halfPrice.setEndTime(endTime);
                halfPrice.setShopId(shopId);
                if (activityId != null) {
                    halfPrice.setId(activityId);
                }
                num = halfPriceMapper.verifyTime(halfPrice);
                break;

            case MINUS:
                EsMinus minus = new EsMinus();
                minus.setStartTime(startTime);
                minus.setEndTime(endTime);
                minus.setShopId(shopId);
                if (activityId != null) {
                    minus.setId(activityId);
                }
                num = minusMapper.verifyTime(minus);
                break;

            case FULL_DISCOUNT:
                EsFullDiscount fullDiscount = new EsFullDiscount();
                fullDiscount.setStartTime(startTime);
                fullDiscount.setEndTime(endTime);
                fullDiscount.setShopId(shopId);
                if (activityId != null) {
                    fullDiscount.setId(activityId);
                }
                num = fullDiscountMapper.verifyTime(fullDiscount);
                break;

            case SECKILL:
                EsSeckill seckill = new EsSeckill();
                seckill.setStartDay(startTime);
                if (activityId != null) {
                    seckill.setId(activityId);
                }
                num = seckillMapper.verifyTime(seckill);
                break;
            default:
                break;
        }

        if (num > 0) {
            throw new ArgumentException(TradeErrorCode.ACTIVITY_TIME_CONFLICT.getErrorCode(),
                    TradeErrorCode.ACTIVITY_TIME_CONFLICT.getErrorMsg());
        }
    }
}


