package com.xdl.jjg.manager.plugin;

import com.shopx.common.util.MathUtil;
import com.shopx.trade.api.model.domain.vo.CartItemsVO;
import com.shopx.trade.api.model.domain.vo.CartVO;
import com.shopx.trade.api.model.domain.vo.PreferentialMessageVO;
import com.shopx.trade.api.model.domain.vo.TradePromotionGoodsVO;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.web.manager.event.PromotionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 单品立减插件
 *
 * @author mengyuanming
 * @version v1.0
 * @date 2017年8月18日下午9:15:00
 * @since v6.4.0
 */
@Component
public class MinusPlugin implements PromotionEvent {
    private static Logger logger = LoggerFactory.getLogger(MinusPlugin.class);

    /**
     * 计算购物车价格
     *
     * @param cart 购物车信息
     * @return ApiResponse
     */
    @Override
    public void onPromotionPriceProcess(CartVO cart) {

        List<CartItemsVO> skuList = cart.getCartItemsList();

        // 过滤 包含选中 且该商品是单品立减活动的购物车项
        List<CartItemsVO> itemCollect = skuList.stream()
                .filter(skuVO -> PromotionTypeEnum.MINUS.name()
                        .equals(skuVO.getPromotionType())).collect(Collectors.toList());

        AtomicReference<Double> goodsDiscountPrice = new AtomicReference<>(0.0);
            itemCollect.forEach(skuVO -> {
                if (skuVO.getChecked() == 1){
                    PreferentialMessageVO preferentialMessageVO = new PreferentialMessageVO();
                    // 过滤出单品立减商品活动

                    List<TradePromotionGoodsVO> promotionCollect = skuVO.getPromotionList().stream()
                            .filter(tradePromotionGoodsVO -> tradePromotionGoodsVO.getIsCheck() == 1 && PromotionTypeEnum.MINUS.name()
                                    .equals(tradePromotionGoodsVO .getPromotionType())).collect(Collectors.toList());

                    promotionCollect.forEach(tradePromotionGoodsVO -> {
                        //商品原价
                        Double originalPrice = skuVO.getGoodsPrice();
                        // 商品数量
                        Integer num = skuVO.getNum();
                        // 商品小计
                        Double subTotal = MathUtil.multiply(originalPrice, num);

                        //商品优惠后的单价
                        Double purchasePrice = MathUtil.subtract(originalPrice, tradePromotionGoodsVO.getMinus().getSingleReductionValue());
                        if (purchasePrice.intValue() < 0) {
                            purchasePrice = 0d;
                        }
                        //设置商品的小计(商品的单价*数量)
                        skuVO.setSubtotal(MathUtil.multiply(purchasePrice,num));
                        skuVO.setThisGoodsPrice(MathUtil.multiply(purchasePrice,num));
                        //此商品的总优惠金额 (商品原价*数量 - 商品优惠后价格*数量)
                        goodsDiscountPrice.getAndSet(MathUtil.subtract(MathUtil.multiply(originalPrice, num), MathUtil.multiply(purchasePrice, num)));
                        // 该商品优惠的总价
//                        skuVO.setThisGoodsPrice(MathUtil.add(this,goodsDiscountPrice.get()));
                        // 设置优惠信息
                        preferentialMessageVO.setPreferentialPrice(goodsDiscountPrice.get());
                        preferentialMessageVO.setPreferentialPic(1);
                        preferentialMessageVO.setPreferentialTotal(subTotal);
                        preferentialMessageVO.setCheckNum(itemCollect.size());
                        skuVO.setPreferentialMessage(preferentialMessageVO);

                        //累加店铺优惠金额
                        cart.getPrice().setDiscountPrice(MathUtil.add(cart.getPrice().getDiscountPrice(), goodsDiscountPrice));

                        logger.info("单品立减优惠金额：{}",goodsDiscountPrice.get());
                    });
                }else {
                    skuVO.setPreferentialMessage(new PreferentialMessageVO());
                }
            });


    }
}
