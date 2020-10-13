package com.xdl.jjg.manager.plugin;

import com.shopx.common.util.MathUtil;
import com.shopx.trade.api.model.domain.vo.CartItemsVO;
import com.shopx.trade.api.model.domain.vo.CartVO;
import com.shopx.trade.api.model.domain.vo.PreferentialMessageVO;
import com.shopx.trade.api.model.domain.vo.TradePromotionGoodsVO;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.web.manager.event.PromotionEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 第二件半价插件
 *
 * @author Snow create in 2018/5/23
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class HalfPricePlugin implements PromotionEvent {

    /**
     * 第二件半价促销活动的计算接口
     * 1.循环购物车中的商品
     * 2.循环判断购物车中的商品是否参加第二件半价活动，判断是否参加
     * 3.然后判断商品是否超过2件，如果超过或等于就计算出要优惠的半价金额，追加到优惠金额中
     * 4.然后将小计金额减去参加这次活动所要减去的金额
     *
     * @param cart 传入购物车参加本活动的商品信息
     */
    @Override
    public void onPromotionPriceProcess(CartVO cart) {

        List<CartItemsVO> skuList = cart.getCartItemsList();

        List<CartItemsVO> collect = skuList.stream()
                .filter(skuVO -> PromotionTypeEnum.HALF_PRICE.name()
                        .equals(skuVO.getPromotionType())).collect(Collectors.toList());
        collect.forEach(skuVO -> {
            if (skuVO.getChecked() == 1){
                    // 单个商品成交价 =
                    Double purchasePrice = skuVO.getGoodsPrice();
                    // 商品数量
                    Integer num = skuVO.getNum();
                    // 商品小计
                    Double subTotal = MathUtil.multiply(purchasePrice, num);

                    // 判断该购物车商品是否是俩件
                    if (num > 1) {
                        // 获取该商品参加的单品活动列表
                        List<TradePromotionGoodsVO> promotionGoodsList = skuVO.getPromotionList();

                        promotionGoodsList.stream()
                                .filter(promotionGoodsVO -> promotionGoodsVO.getPromotionType().equals(PromotionTypeEnum.HALF_PRICE.name()) && promotionGoodsVO.getIsCheck() == 1)
                                .forEach(promotionGoodsVO -> {
                                    // 要优惠的价格
                                    Double halfPrice = MathUtil.divide(purchasePrice, 2);
                                    BigDecimal singlePrice = new BigDecimal("0");
                                    //优惠价格
                                    Double add = MathUtil.add(cart.getPrice().getDiscountPrice(), halfPrice);
                                    BigDecimal bigDecimal1 = singlePrice.add(new BigDecimal(add.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    // 设置优惠信息
                                    skuVO.setPreferentialMessage(new PreferentialMessageVO());
                                    skuVO.getPreferentialMessage().setPreferentialPrice(halfPrice);
                                    skuVO.getPreferentialMessage().setPreferentialTotal(subTotal);
                                    skuVO.getPreferentialMessage().setCheckNum(collect.size());
                                    // 将优惠价格追加到原优惠价格当中
                                    cart.getPrice().setDiscountPrice(bigDecimal1.doubleValue());
                                    BigDecimal bigDecimal2 = singlePrice.add(new BigDecimal(subTotal.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    // 将小计金额减去参加这次活动所要优惠的金额
                                    skuVO.setSubtotal(bigDecimal2.doubleValue());

                                });
                    }else {
                        skuVO.setPreferentialMessage(new PreferentialMessageVO());
                        skuVO.getPreferentialMessage().setPreferentialPrice(0.0);
                        skuVO.getPreferentialMessage().setPreferentialTotal(subTotal);
                        skuVO.getPreferentialMessage().setCheckNum(collect.size());
                    }
            }else {
                skuVO.setPreferentialMessage(new PreferentialMessageVO());
            }
        });
    }
}
