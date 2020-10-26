package com.xdl.jjg.manager.plugin;

import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.jjg.trade.model.vo.CartItemsVO;
import com.jjg.trade.model.vo.CartVO;
import com.jjg.trade.model.vo.PreferentialMessageVO;
import com.jjg.trade.model.vo.TradePromotionGoodsVO;
import com.xdl.jjg.manager.event.PromotionEvent;
import com.xdl.jjg.util.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: CompanyDiscount
 * @Description: 商品折扣活动计算插件
 * @Author: libw  981087977@qq.com
 * @Date: 6/26/2019 15:13
 * @Version: 1.0
 */
@Component
public class GoodsDiscountPlugin implements PromotionEvent {
    private static Logger logger = LoggerFactory.getLogger(GoodsDiscountPlugin.class);

    /**
     * 计算购物车价格
     *
     * @param cart 购物车信息
     * @return ApiResponse
     */

    @Override
    public void onPromotionPriceProcess(CartVO cart) {

        List<CartItemsVO> itemsVOList = cart.getCartItemsList().stream()
                .filter(skuVO -> PromotionTypeEnum.GOODS_DISCOUNT.name()
                        .equals(skuVO.getPromotionType())).collect(Collectors.toList());

        itemsVOList.forEach(skuVO -> {

            List<TradePromotionGoodsVO> collect = skuVO.getPromotionList().stream()
                    .filter(promotionGoodsVO -> promotionGoodsVO.getPromotionType().equals(PromotionTypeEnum.GOODS_DISCOUNT.name()) && promotionGoodsVO.getIsCheck() == 1).collect(Collectors.toList());
            if (skuVO.getChecked() == 1){
                collect.forEach(promotionGoodsVO -> {
                    // 商品原价
                    Double originalPrice = skuVO.getGoodsPrice();
                    // 商品数量
                    int num = skuVO.getNum();
                    // 商品优惠后单价
                    Double purchasePrice = MathUtil.multiply(originalPrice, promotionGoodsVO.getGoodsDiscounts());

                    Double multiply = MathUtil.multiply(originalPrice, num);
                    // 设置商品的小计(商品的单价*数量)
                    skuVO.setSubtotal(multiply);
                    // 设置商品折扣信息
                    Double discountPrice = MathUtil.subtract(originalPrice, purchasePrice);
                    skuVO.setPreferentialMessage(new PreferentialMessageVO());
                    skuVO.getPreferentialMessage().setPreferentialDiscount(promotionGoodsVO.getGoodsDiscounts());
                    skuVO.getPreferentialMessage().setPreferentialPrice(MathUtil.multiply(discountPrice,num));
                    skuVO.getPreferentialMessage().setCheckNum(itemsVOList.size());
                    skuVO.getPreferentialMessage().setPreferentialTotal(multiply);
                    // 此商品的总优惠金额 (商品原价*数量 - 商品优惠后价格*数量)
                    Double goodsDiscountPrice = MathUtil.subtract(MathUtil.multiply(originalPrice, num), MathUtil.multiply(purchasePrice, num));
                    // 该商品优惠后的总价
                    skuVO.setThisGoodsPrice(MathUtil.divide(multiply,goodsDiscountPrice));
                    // 累加店铺优惠金额
                    cart.getPrice().setDiscountPrice(MathUtil.add(cart.getPrice().getDiscountPrice(), goodsDiscountPrice));
                    logger.info("商品打折优惠价格：{}",goodsDiscountPrice);
                });
            }else {
                skuVO.setPreferentialMessage(new PreferentialMessageVO());
            }

        });

    }
}
