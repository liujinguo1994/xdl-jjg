package com.xdl.jjg.manager;

import cn.hutool.core.date.DateUtil;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.goods.api.model.domain.cache.EsGoodsCO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.member.api.model.domain.EsDiscountDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.service.IEsDiscountService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.api.model.domain.*;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.api.service.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: PromotionGoodsManager
 * @Description: 活动商品业务类
 * @Author: libw  981087977@qq.com
 * @Date: 6/13/2019 09:23
 * @Version: 1.0
 */
@Component
public class PromotionGoodsManager {

    @Autowired
    private JedisCluster jedisCluster;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFullDiscountService fullDiscountService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsPromotionGoodsService promotionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsFullDiscountGiftService fullDiscountGiftService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMinusService minusService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsHalfPriceService halfPriceService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsDiscountService goodsDiscountService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService couponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService goodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsDiscountService iEsDiscountService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSeckillApplyService iEsSeckillApplyService;

    @Autowired
    private SeckillManager seckillManager;

    /**
     * 获取所有商品的有效活动
     *
     *
     * @param goodsId   商品id
     * @param memberId        会员ID
     *
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/06/15 15:05:27
     * @return: com.shopx.common.model.result.ApiResponse
     *
     * @updateBy: KL
     * @updateDesc: 修改为对应sku的优惠活动集合
     * @updateDate: 2020-4-27 13:31:26
     */
    protected List<PromotionVO> getPromotion(Long goodsId, Long skuId, Long memberId) {

        long currTime = System.currentTimeMillis();

        List<PromotionVO> promotionList = new ArrayList<>();

        // 根据会员id查询企业标识符
        DubboResult memberResult = memberService.getMember(memberId);

        if (!memberResult.isSuccess()) {
            throw new ArgumentException(memberResult.getCode(), memberResult.getMsg());
        }

        // 缓存里查询商品信息
        DubboResult goodsResult = this.goodsService.getEsGoods(goodsId);
        if (!goodsResult.isSuccess()) {
            throw new ArgumentException(goodsResult.getCode(), goodsResult.getMsg());
        }

        // 查询商品所有活动
        DubboResult resultList = this.promotionGoodsService.getAllPromotionGoods(goodsId, currTime,
                ((EsGoodsCO) goodsResult.getData()).getShopId(),skuId);
        if (!resultList.isSuccess()) {
            throw new ArgumentException(resultList.getCode(), resultList.getMsg());
        }
        List<EsPromotionGoodsDO> promotionGoodsListDO = (List<EsPromotionGoodsDO>) resultList.getData();
        // 活动SkuId和传入的skuId不同，且优惠类型不是满减满赠的筛选掉
        promotionGoodsListDO = promotionGoodsListDO.stream().filter(p -> (p.getSkuId() != null && p.getSkuId().longValue() == skuId.longValue())
                || PromotionTypeEnum.FULL_DISCOUNT.name().equals(p.getPromotionType()) || p.getSkuId() == -1).collect(Collectors.toList());
        // 秒杀筛选，如果未在秒杀阶段，去掉秒杀活动
        int tempSeckillingTimeline = -1;
        DubboResult<EsSeckillTimetableDO> inSeckillNow = iEsSeckillApplyService.getInSeckillNow();
        if(inSeckillNow.isSuccess()){
            tempSeckillingTimeline = inSeckillNow.getData().getTimeline();
        }
        int finalTempSeckillingTimeline = tempSeckillingTimeline;
        promotionGoodsListDO = promotionGoodsListDO.stream().filter(p -> {
            if(!PromotionTypeEnum.SECKILL.name().equals(p.getPromotionType())){
                return true;
            }
            int hour = DateUtil.hour(DateUtil.date(p.getStartTime()), true);
            return hour == finalTempSeckillingTimeline;
        }).collect(Collectors.toList());

        EsMemberDO esMemberDO = (EsMemberDO)memberResult.getData();
        // 会员公司标识符不为空的场合
        if (!StringUtil.isEmpty(esMemberDO.getCompanyCode())) {
            EsPromotionGoodsDO esPromotionGoodsDO = new EsPromotionGoodsDO();
            esPromotionGoodsDO.setPromotionType(PromotionTypeEnum.COMPANY_DISCOUNT.name());
            esPromotionGoodsDO.setCompanyCode(((EsMemberDO) memberResult.getData()).getCompanyCode());
            esPromotionGoodsDO.setGoodsId(goodsId);
            esPromotionGoodsDO.setActivityId(999999999l);
            promotionGoodsListDO.add(esPromotionGoodsDO);
        }
        // 遍历活动，组装所有活动信息
        for (EsPromotionGoodsDO promotionGoodsDO : promotionGoodsListDO) {
            PromotionVO promotionVO = new PromotionVO();
            BeanUtils.copyProperties(promotionGoodsDO, promotionVO);

            // 组装活动
            boolean isNull = this.assemblePromotion(promotionVO);

            // 活动不为类型不为空，并且VO里所有活动不为null的情况，添加到活动列表
            if (promotionVO.getPromotionType() != null && isNull) {
                promotionList.add(promotionVO);
            }
        }

        return promotionList;
    }


    protected List<PromotionVO> getMqPromotionChange(Long goodsId, Long skuId) {

        long currTime = System.currentTimeMillis();
        List<PromotionVO> promotionList = new ArrayList<>();

        // 缓存里查询商品信息
        DubboResult goodsResult = this.goodsService.getEsGoods(goodsId);
        if (!goodsResult.isSuccess()) {
            throw new ArgumentException(goodsResult.getCode(), goodsResult.getMsg());
        }

        // 查询商品所有活动
        DubboResult resultList = this.promotionGoodsService.getAllPromotionGoods(goodsId, currTime,
                ((EsGoodsCO) goodsResult.getData()).getShopId(),skuId);
        if (!resultList.isSuccess()) {
            throw new ArgumentException(resultList.getCode(), resultList.getMsg());
        }
        List<EsPromotionGoodsDO> promotionGoodsListDO = (List<EsPromotionGoodsDO>) resultList.getData();
        // 活动SkuId和传入的skuId不同，且优惠类型不是满减满赠的筛选掉
        promotionGoodsListDO = promotionGoodsListDO.stream().filter(p -> (p.getSkuId() != null && p.getSkuId().longValue() == skuId.longValue())
                || PromotionTypeEnum.FULL_DISCOUNT.name().equals(p.getPromotionType())).collect(Collectors.toList());

        // 遍历活动，组装所有活动信息
        for (EsPromotionGoodsDO promotionGoodsDO : promotionGoodsListDO) {
            PromotionVO promotionVO = new PromotionVO();
            BeanUtils.copyProperties(promotionGoodsDO, promotionVO);

            // 组装活动
            boolean isNull = this.assemblePromotion(promotionVO);

            // 活动不为类型不为空，并且VO里所有活动不为null的情况，添加到活动列表
            if (promotionVO.getPromotionType() != null && isNull) {
                promotionList.add(promotionVO);
            }
        }

        return promotionList;
    }

    /**
     * @param promotionVO 活动VO
     * @author: libw 981087977@qq.com
     * @date: 2019/06/27 11:18:08
     * @return: com.shopx.common.model.result.ApiResponse
     */
    private void addCompanyDiscount(PromotionVO promotionVO) {
        // 判断活动类型是不是公司折扣
        if (PromotionTypeEnum.COMPANY_DISCOUNT.name().equals(promotionVO.getPromotionType())) {
            promotionVO.setDiscount(null);
            // 通过goodsId获取商品信息
            DubboResult<EsGoodsCO> goodsResult = goodsService.getEsBuyerGoods(promotionVO.getGoodsId());

            if (!goodsResult.isSuccess()) {
                throw new ArgumentException(goodsResult.getCode(), goodsResult.getMsg());
            }
            // 通过商品分类和公司标识符获取折扣
            DubboResult<EsDiscountDO> discount1 = iEsDiscountService.getDiscountByCompanyCodeAndCategoryId(promotionVO.getCompanyCode(),
                    goodsResult.getData().getCategoryId());
            if (discount1.isSuccess() && discount1 != null){
                promotionVO.setDiscount(discount1.getData().getDiscount());
            }
        }
    }

    /**
     * 组装所有商品活动详情
     *
     * @param promotionVO 活动VO
     * @author: libw 981087977@qq.com
     * @date: 2019/06/15 15:31:13
     * @return: com.shopx.common.model.result.ApiResponse
     */
    private boolean assemblePromotion(PromotionVO promotionVO) {
        boolean result = false;
        // 判断类型是不是满优惠活动
        if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(promotionVO.getPromotionType())) {
            full:
            {
                // 查询满减活动
                DubboResult<EsFullDiscountDO> fullDiscountResult = this.fullDiscountService.getFullDiscountForCache(promotionVO.getActivityId());
                if (!fullDiscountResult.isSuccess()) {
                    throw new ArgumentException(fullDiscountResult.getCode(), fullDiscountResult.getMsg());
                }
                EsFullDiscountDO esFullDiscountDO = fullDiscountResult.getData();
                // 如果已经停用，则跳出整个判断
                if (esFullDiscountDO ==null || esFullDiscountDO.getIsDel() == null ||esFullDiscountDO.getIsDel() != 0) {
                    break full;

                 }
                EsFullDiscountVO fullDiscountVO = new EsFullDiscountVO();
                BeanUtil.copyProperties(esFullDiscountDO, fullDiscountVO);
                // 判断是不是赠赠品
                if (esFullDiscountDO.getIsSendGift() != null && esFullDiscountDO.getIsSendGift() == 1 && esFullDiscountDO.getGiftId() != null) {
                    // 获取赠品信息
                    DubboResult fullDiscountGiftResult = fullDiscountGiftService.getFullDiscountGift(fullDiscountVO.getGiftId());
                    if (!fullDiscountGiftResult.isSuccess()) {
                        throw new ArgumentException(fullDiscountGiftResult.getCode(), fullDiscountGiftResult.getMsg());
                    }
                    EsFullDiscountGiftVO esFullDiscountGiftVO = new EsFullDiscountGiftVO();
                    EsFullDiscountGiftDO esFullDiscountGiftDO = (EsFullDiscountGiftDO) fullDiscountGiftResult.getData();

                    BeanUtil.copyProperties(esFullDiscountGiftDO, esFullDiscountGiftVO);
                    fullDiscountVO.setFullDiscountGift(esFullDiscountGiftVO);
                }
                // 判断是否赠品优惠券
                if (esFullDiscountDO.getIsSendBonus() != null && esFullDiscountDO.getIsSendBonus() == 1 && esFullDiscountDO.getBonusId() != null) {
                    // 获取优惠券信息
                    DubboResult couponResult = this.couponService.getCoupon(fullDiscountVO.getBonusId());
                    if (couponResult.isSuccess() && couponResult.getData() != null ) {
                        EsCouponVO couponVO = new EsCouponVO();
                        EsCouponDO esCouponDO= (EsCouponDO) couponResult.getData();
                        BeanUtil.copyProperties(esCouponDO, couponVO);
                        fullDiscountVO.setCoupon(couponVO);
                    }
                }
                promotionVO.setFullDiscount(fullDiscountVO);
                result = true;
            }
        }
        // 判断类型是不是单品立减活动
        if (PromotionTypeEnum.MINUS.name().equals(promotionVO.getPromotionType())) {
            DubboResult<EsMinusDO> minusResult = this.minusService.getMinusForCache(promotionVO.getActivityId());
            if (!minusResult.isSuccess()) {
                throw new ArgumentException(minusResult.getCode(), minusResult.getMsg());
            }
            EsMinusDO esMinusDO = minusResult.getData();
            EsMinusVO minusVO = new EsMinusVO();
            if (esMinusDO != null && esMinusDO.getIsDel() == 0) {
                BeanUtil.copyProperties(esMinusDO, minusVO);
                promotionVO.setMinus(minusVO);
                result = true;
            }

        }
        // 判断类型是不是第二件半价活动
        if (PromotionTypeEnum.HALF_PRICE.name().equals(promotionVO.getPromotionType())) {
            DubboResult<EsHalfPriceDO> halfPriceResult = this.halfPriceService.getHalfPriceForCache(promotionVO.getActivityId());
            if (!halfPriceResult.isSuccess()) {
                throw new ArgumentException(halfPriceResult.getCode(), halfPriceResult.getMsg());
            }
            EsHalfPriceVO halfPriceVO = new EsHalfPriceVO();
            EsHalfPriceDO esHalfPriceDO = halfPriceResult.getData();
            if (esHalfPriceDO !=null && esHalfPriceDO.getIsDel() == 0) {
                BeanUtil.copyProperties(esHalfPriceDO, halfPriceVO);
                promotionVO.setHalfPrice(halfPriceVO);
                result = true;
            }

        }
        // 判断类型是不是限时抢购活动
        if (PromotionTypeEnum.SECKILL.name().equals(promotionVO.getPromotionType())) {
            SeckillGoodsVO seckillGoodsVO = this.seckillManager.getSeckillGoods(promotionVO.getGoodsId());
            if (seckillGoodsVO != null) {
                promotionVO.setSeckillGoods(seckillGoodsVO);
                result = true;
            }

        }
        // 判断类型是不是商品折扣活动
        if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(promotionVO.getPromotionType())) {
            DubboResult goodsDiscountResult = this.goodsDiscountService.getGoodsDiscount(promotionVO.getActivityId());
            if (!goodsDiscountResult.isSuccess()) {
                throw new ArgumentException(goodsDiscountResult.getCode(), goodsDiscountResult.getMsg());
            }
            EsGoodsDiscountVO goodsDiscountVO = new EsGoodsDiscountVO();
            EsGoodsDiscountDO esGoodsDiscountDO = (EsGoodsDiscountDO)goodsDiscountResult.getData();
            BeanUtils.copyProperties(esGoodsDiscountDO,goodsDiscountVO);
            if (goodsDiscountVO.getIsDel() == 0) {
                promotionVO.setGoodsDiscount(goodsDiscountVO);
                result = true;
            }
        }
//        this.addCompanyDiscount(promotionVO);

        // 判断活动类型是不是公司折扣
        if (PromotionTypeEnum.COMPANY_DISCOUNT.name().equals(promotionVO.getPromotionType())) {
            promotionVO.setDiscount(null);
            // 通过goodsId获取商品信息
            DubboResult<EsGoodsCO> goodsResult = goodsService.getEsBuyerGoods(promotionVO.getGoodsId());

            if (!goodsResult.isSuccess()) {
                throw new ArgumentException(goodsResult.getCode(), goodsResult.getMsg());
            }
            // 通过商品分类和公司标识符获取折扣
            DubboResult<EsDiscountDO> discount1 = iEsDiscountService.getDiscountByCompanyCodeAndCategoryId(promotionVO.getCompanyCode(),
                    goodsResult.getData().getCustomId());
            if (discount1.isSuccess() && discount1 != null){
                promotionVO.setDiscount(1.0);
                if (discount1.getData().getDiscount() != null){
                    promotionVO.setDiscount(discount1.getData().getDiscount());
                }

                result = true;
            }
        }
        return result;


    }
}
