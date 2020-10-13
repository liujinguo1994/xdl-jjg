package com.xdl.jjg.manager.plugin;

import com.shopx.common.util.MathUtil;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.web.manager.SeckillManager;
import com.shopx.trade.web.manager.event.PromotionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 限时抢购插件
 *
 * @author Snow create in 2018/5/25
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class SeckillPlugin implements PromotionEvent {

    @Autowired
    private SeckillManager seckillManager;

    /**
     * 计算购物车价格
     *
     * @param cart 购物车信息
     */
    @Override
    public void onPromotionPriceProcess(CartVO cart) {

        // 防止APP端cartPrice展示还是秒杀结束前的
        List<CartItemsVO> skuList = cart.getCartItemsList();
//        skuList.forEach(cartItemsVO -> {
//            cartItemsVO.setCartPrice(cartItemsVO.getGoodsPrice());
//        });

        // 循环购物车商品
        List<CartItemsVO> collect = skuList.stream()
                // 如果商品没有选中，则不进行计算
                .filter(skuVO ->  PromotionTypeEnum.SECKILL.name()
                        .equals(skuVO.getPromotionType())).collect(Collectors.toList());

        collect.forEach(skuVO ->{
            List<TradePromotionGoodsVO> collects = skuVO.getPromotionList().stream()
                    .filter(promotionGoodsVO -> promotionGoodsVO.getPromotionType().equals(PromotionTypeEnum.SECKILL.name()) && promotionGoodsVO.getIsCheck() == 1).collect(Collectors.toList());

            if (skuVO.getChecked() == 1){
                    collects.forEach(tradePromotionGoodsVO -> {
                        SeckillGoodsVO seckillGoodsVO = tradePromotionGoodsVO.getSeckillGoods();
                        int soldNum = seckillGoodsVO.getSoldNum();

                        //售空数量
                        int soldQuantity = seckillGoodsVO.getSoldQuantity();

                        //剩余可售数量
                        int num = soldQuantity - soldNum;

                        //商品原价
                        double originalPrice = skuVO.getGoodsPrice();

                        //原价小计
                        double totalPrice = MathUtil.multiply(originalPrice, skuVO.getNum());

                        //判断活动是否开始 && 用户加入购物车的数量小于等于剩余的数量
                        if (seckillGoodsVO.getIsStart() == 1 && skuVO.getNum() <= num) {

                            //活动价小计
                            double totalActivityPrice = MathUtil.multiply(seckillGoodsVO.getSeckillPrice(), skuVO.getNum());

                            //商品优惠后单价
                            skuVO.setCartPrice(seckillGoodsVO.getSeckillPrice());

                            //商品小计
                            skuVO.setSubtotal(totalActivityPrice);

                            //当前商品优惠的金额
                            double currSkuDiscountPrice = MathUtil.subtract(totalPrice, totalActivityPrice);
                            skuVO.setThisGoodsPrice(totalActivityPrice);
                            skuVO.setPreferentialMessage(new PreferentialMessageVO());
                            skuVO.getPreferentialMessage().setPreferentialPrice(currSkuDiscountPrice);
                            skuVO.getPreferentialMessage().setCheckNum(num);
                            skuVO.getPreferentialMessage().setPreferentialTotal(totalPrice);
                            //店铺总优惠的金额
                            cart.getPrice().setDiscountPrice(MathUtil.add(cart.getPrice().getDiscountPrice(), currSkuDiscountPrice));
                        }
                    });
                }else {
                    //商品优惠后单价
                    skuVO.setPreferentialMessage(new PreferentialMessageVO());
                }
        });
    }
}
