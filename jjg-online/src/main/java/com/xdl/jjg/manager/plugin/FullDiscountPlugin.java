package com.xdl.jjg.manager.plugin;


import com.jjg.trade.model.domain.EsCouponDO;
import com.jjg.trade.model.domain.EsFullDiscountDO;
import com.jjg.trade.model.domain.EsFullDiscountGiftDO;
import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.jjg.trade.model.vo.*;
import com.xdl.jjg.manager.event.PromotionEvent;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.web.service.IEsCouponService;
import com.xdl.jjg.web.service.IEsFullDiscountGiftService;
import com.xdl.jjg.web.service.IEsFullDiscountService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 满优惠插件
 *
 * @author Snow create in 2018/5/25
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class FullDiscountPlugin implements PromotionEvent {
    private static Logger logger = LoggerFactory.getLogger(FullDiscountPlugin.class);

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsFullDiscountService fullDiscountService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsFullDiscountGiftService fullDiscountGiftService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCouponService couponService;

    @Override
    public void onPromotionPriceProcess(CartVO cart) {

        List<CartItemsVO> skuList = cart.getCartItemsList();
        // 声明map用于存储促销活动对应参与此活动的商品的总价
        Map<Long, Double> map = new HashMap<>(16);
        List<CartItemsVO> collect = skuList.stream()
                .filter(skuVO -> PromotionTypeEnum.FULL_DISCOUNT.name()
                        .equals(skuVO.getPromotionType())).collect(Collectors.toList());
        List<CartItemsVO> collect1 = collect.stream().sorted(Comparator.comparing(CartItemsVO::getChecked)).collect(Collectors.toList());
        collect1.forEach(skuVO ->{
                    // 取出商品参加的活动列表
                    List<TradePromotionGoodsVO> promotionList = skuVO.getPromotionList();
                    if(promotionList!=null && promotionList.size()>0) {
                        for (TradePromotionGoodsVO promotionGoodsVO : promotionList) {
                            Long activityId = promotionGoodsVO.getActivityId();
                            if(PromotionTypeEnum.FULL_DISCOUNT.name().equals(promotionGoodsVO.getPromotionType()) && skuVO.getChecked() == 1) {
                                Double price = map.get(activityId) == null? 0 : (Double)map.get(activityId);
                                map.put(activityId, MathUtil.add(skuVO.getThisGoodsPrice() , price));
                            }else if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(promotionGoodsVO.getPromotionType()) && skuVO.getChecked() == 0){
                                map.put(activityId,0.0);
                            }
                        }
                    }
                    skuVO.setPreferentialMessage(new PreferentialMessageVO());
                });

        if(map.size() > 0) {
            for (Map.Entry<Long, Double> entry : map.entrySet()) {
                Long activityId = entry.getKey();
                Double price = entry.getValue();
                // 声明变量用于计算优惠价格
                Double discountPrice = 0.00;
                // 获取活动信息
                DubboResult<EsFullDiscountDO> result = this.fullDiscountService.getFullDiscountForCache(activityId);
                if (result.isSuccess() && result.getData() != null) {
                    EsFullDiscountDO fullDiscountDO = result.getData();
                    List<CartItemsVO> count = skuList.stream()
                            .filter(skuVO -> skuVO.getChecked() == 1 && PromotionTypeEnum.FULL_DISCOUNT.name()
                                    .equals(skuVO.getPromotionType())).collect(Collectors.toList());
                    // 判断是否达到优惠条件
                    if (fullDiscountDO.getFullMoney() != null && fullDiscountDO.getFullMoney() <= price && fullDiscountDO.getIsDel() == 0) {

                        // 满减优惠计算
                        if (fullDiscountDO.getIsFullMinus() != null && fullDiscountDO.getIsFullMinus() == 1 && fullDiscountDO.getMinusValue() != null) {
                            discountPrice += fullDiscountDO.getMinusValue();

                            for (CartItemsVO skuVO : skuList) {
                                if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(skuVO.getPromotionType())) {
                                    skuVO.getPreferentialMessage().setPreferentialType("FullMinus");
                                    skuVO.getPreferentialMessage().setPreferentialTotal(price);
                                    skuVO.getPreferentialMessage().setPreferentialPic(1);
                                    skuVO.getPreferentialMessage().setCheckNum(count.size());
                                    skuVO.getPreferentialMessage().setPreferentialThreshold(fullDiscountDO.getFullMoney());
                                    skuVO.getPreferentialMessage().setPreferentialPrice(discountPrice);
                                    skuVO.getPreferentialMessage().setDifference(0.0);
//                                    skuVO.setCartPrice(skuVO.getGoodsPrice());
                                    skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(),skuVO.getNum()));
                                    logger.info("满减优惠价格：{}",discountPrice);
                                }
                            }
                        }
                        // 满折优惠计算
                        if (fullDiscountDO.getIsDiscount() != null && fullDiscountDO.getIsDiscount() == 1 && fullDiscountDO.getDiscountValue() != null) {
                            discountPrice += price - MathUtil.multiply(price, MathUtil.multiply(fullDiscountDO.getDiscountValue(), 0.1));
                            for (CartItemsVO skuVO : skuList) {
                                if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(skuVO.getPromotionType())) {
                                    skuVO.getPreferentialMessage().setPreferentialType("FullDiscount");
                                    skuVO.getPreferentialMessage().setPreferentialTotal(price);
                                    skuVO.getPreferentialMessage().setPreferentialPic(1);
                                    skuVO.getPreferentialMessage().setCheckNum(count.size());
                                    skuVO.getPreferentialMessage().setPreferentialThreshold(fullDiscountDO.getFullMoney());
                                    skuVO.getPreferentialMessage().setPreferentialDiscount(fullDiscountDO.getDiscountValue());
                                    skuVO.getPreferentialMessage().setPreferentialPrice(discountPrice);
                                    skuVO.getPreferentialMessage().setDifference(0.0);
//                                    skuVO.setCartPrice(skuVO.getGoodsPrice());
                                    skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(),skuVO.getNum()));
                                    logger.info("满折折扣比例：{};满折优惠金额：{}",fullDiscountDO.getDiscountValue(),discountPrice);
                                }
                            }
                        }
                        // 免运费优惠计算
                        if (fullDiscountDO.getIsFreeShip() != null && fullDiscountDO.getIsFreeShip() == 1) {
                            cart.getPrice().setIsFreeFreight(1);
                            cart.getPrice().setFreightPrice(0d);
                            for (CartItemsVO skuVO : skuList) {
                                if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(skuVO.getPromotionType())) {
                                    skuVO.getPreferentialMessage().setPreferentialType("FullFreeShip");
                                    skuVO.getPreferentialMessage().setPreferentialTotal(price);
                                    skuVO.getPreferentialMessage().setPreferentialPic(1);
                                    skuVO.getPreferentialMessage().setCheckNum(count.size());
                                    skuVO.getPreferentialMessage().setPreferentialThreshold(fullDiscountDO.getFullMoney());
                                    skuVO.getPreferentialMessage().setDifference(0.0);
//                                    skuVO.setCartPrice(skuVO.getGoodsPrice());
                                    skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(),skuVO.getNum()));
                                }
                            }
                        }
                        // 判断是否赠送赠品
                        if (fullDiscountDO.getIsSendGift() != null && fullDiscountDO.getIsSendGift() == 1 && fullDiscountDO.getGiftId() != null) {
                            EsFullDiscountGiftDO giftDO = fullDiscountGiftService.getFullDiscountGift(fullDiscountDO.getGiftId()).getData();
                            // 可用库存大于0
                            if (giftDO != null && giftDO.getEnableStore() > 0) {
                                EsFullDiscountGiftVO fullDiscountGiftVO = new EsFullDiscountGiftVO();
                                BeanUtil.copyProperties(giftDO, fullDiscountGiftVO);
                                cart.getGiftList().add(fullDiscountGiftVO);
                                for (CartItemsVO skuVO : skuList) {
                                    if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(skuVO.getPromotionType())) {
                                        skuVO.getPreferentialMessage().setPreferentialType("FullSendGift");
                                        skuVO.getPreferentialMessage().setPreferentialTotal(price);
                                        skuVO.getPreferentialMessage().setPreferentialPic(1);
                                        skuVO.getPreferentialMessage().setCheckNum(count.size());
                                        skuVO.getPreferentialMessage().setPreferentialThreshold(fullDiscountDO.getFullMoney());
                                        skuVO.getPreferentialMessage().setEsSendDiscountGift(fullDiscountGiftVO);
                                        skuVO.getPreferentialMessage().setDifference(0.0);
//                                        skuVO.setCartPrice(skuVO.getGoodsPrice());
                                        skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(),skuVO.getNum()));
                                    }
                                }
                            }
                        }
                        // 判断是否赠品优惠券
                        if (fullDiscountDO.getIsSendBonus() != null && fullDiscountDO.getIsSendBonus() == 1 && fullDiscountDO.getBonusId() != null) {

                            DubboResult couponResult = this.couponService.getCoupon(fullDiscountDO.getBonusId());
                            if (couponResult.isSuccess() && couponResult.getData() != null) {
                                EsCouponDO esCouponDO = (EsCouponDO) couponResult.getData();
                                EsCouponVO couponVO = new EsCouponVO();
                                BeanUtil.copyProperties(esCouponDO, couponVO);
                                //已被领取的数量小于发行量 则可以领取
                                if (couponVO.getReceivedNum() < couponVO.getCreateNum()) {
                                    cart.getGiftCouponList().add(couponVO);
                                    for (CartItemsVO skuVO : skuList) {
                                        if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(skuVO.getPromotionType())) {
                                            skuVO.getPreferentialMessage().setPreferentialType("FullSendBonus");
                                            skuVO.getPreferentialMessage().setPreferentialTotal(price);
                                            skuVO.getPreferentialMessage().setPreferentialPic(1);
                                            skuVO.getPreferentialMessage().setCheckNum(count.size());
                                            skuVO.getPreferentialMessage().setPreferentialThreshold(fullDiscountDO.getFullMoney());
                                            skuVO.getPreferentialMessage().setEsSendCoupon(couponVO);
                                            skuVO.getPreferentialMessage().setDifference(0.0);
//                                            skuVO.setCartPrice(skuVO.getGoodsPrice());
                                            skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(),skuVO.getNum()));
                                        }
                                    }
                                }
                            }
                        }
                        cart.getPrice().setDiscountPrice(MathUtil.add(cart.getPrice().getDiscountPrice(), discountPrice));
                    } else {
                        if (fullDiscountDO.getIsFullMinus() != null && fullDiscountDO.getIsFullMinus() == 1 && fullDiscountDO.getMinusValue() != null) {
                            for (CartItemsVO skuVO : skuList) {
                                if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(skuVO.getPromotionType())) {
                                    // 设置活动参数
                                    skuVO.getPreferentialMessage().setPreferentialType("FullMinus");
                                    // 优惠门槛金额
                                    skuVO.getPreferentialMessage().setPreferentialThreshold(fullDiscountDO.getFullMoney());
                                    skuVO.getPreferentialMessage().setPreferentialTotal(price);
                                    skuVO.getPreferentialMessage().setCheckNum(count.size());
                                    // 差额
                                    skuVO.getPreferentialMessage().setDifference(MathUtil.subtract(fullDiscountDO.getFullMoney(),price));
                                    skuVO.getPreferentialMessage().setPreferentialPrice(null);
//                                    skuVO.setCartPrice(skuVO.getGoodsPrice());
                                    skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(),skuVO.getNum()));
                                }
                            }
                        }
                        // 满折优惠计算
                        if (fullDiscountDO.getIsDiscount() != null && fullDiscountDO.getIsDiscount() == 1 && fullDiscountDO.getDiscountValue() != null) {
                            for (CartItemsVO skuVO : skuList) {
                                if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(skuVO.getPromotionType())) {
                                    // 设置活动参数
                                    skuVO.getPreferentialMessage().setPreferentialType("FullDiscount");
                                    // 优惠门槛金额
                                    skuVO.getPreferentialMessage().setPreferentialThreshold(fullDiscountDO.getFullMoney());
                                    skuVO.getPreferentialMessage().setPreferentialTotal(price);
                                    skuVO.getPreferentialMessage().setCheckNum(count.size());
                                    // 差额
                                    skuVO.getPreferentialMessage().setDifference(MathUtil.subtract(fullDiscountDO.getFullMoney(),price));
//                                    skuVO.setCartPrice(skuVO.getGoodsPrice());
                                    skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(),skuVO.getNum()));
                                }
                            }
                        }
                        // 判断是否赠送赠品
                        if (fullDiscountDO.getIsSendGift() != null && fullDiscountDO.getIsSendGift() == 1 && fullDiscountDO.getGiftId() != null) {
                            EsFullDiscountGiftDO giftDO = fullDiscountGiftService.getFullDiscountGift(fullDiscountDO.getGiftId()).getData();
                            // 可用库存大于0
                            if (giftDO != null && giftDO.getEnableStore() > 0) {
                                EsFullDiscountGiftVO fullDiscountGiftVO = new EsFullDiscountGiftVO();
                                BeanUtil.copyProperties(giftDO, fullDiscountGiftVO);
//                                cart.getGiftList().add(fullDiscountGiftVO);
                                for (CartItemsVO skuVO : skuList) {
                                    if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(skuVO.getPromotionType())) {
                                        // 设置活动参数
                                        skuVO.getPreferentialMessage().setPreferentialType("FullSendGift");
                                        // 优惠门槛金额
                                        skuVO.getPreferentialMessage().setPreferentialThreshold(fullDiscountDO.getFullMoney());
                                        skuVO.getPreferentialMessage().setPreferentialTotal(price);
                                        skuVO.getPreferentialMessage().setCheckNum(count.size());
                                        // 差额
                                        skuVO.getPreferentialMessage().setDifference(MathUtil.subtract(fullDiscountDO.getFullMoney(),price));
//                                        skuVO.setCartPrice(skuVO.getGoodsPrice());
                                        skuVO.setSubtotal(MathUtil.multiply(skuVO.getGoodsPrice(),skuVO.getNum()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        skuList.sort(Comparator.comparing(CartItemsVO::getPromotionType));
        cart.setCartItemsList(skuList);

    }
}
