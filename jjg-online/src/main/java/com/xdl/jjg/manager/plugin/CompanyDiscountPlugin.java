package com.xdl.jjg.manager.plugin;

import com.shopx.common.util.MathUtil;
import com.shopx.trade.api.model.domain.vo.CartItemsVO;
import com.shopx.trade.api.model.domain.vo.CartVO;
import com.shopx.trade.api.model.domain.vo.TradePromotionGoodsVO;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import com.shopx.trade.web.manager.event.PromotionEvent;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: CompanyDiscount
 * @Description: 公司折扣打折活动
 * @Author: libw  981087977@qq.com
 * @Date: 6/26/2019 15:13
 * @Version: 1.0
 */
@Component
public class CompanyDiscountPlugin implements PromotionEvent {

    /**
     * 计算购物车价格
     *
     * @param cart 购物车信息
     * @return ApiResponse
     */

    @Override
    public void onPromotionPriceProcess(CartVO cart) {

        List<CartItemsVO> skuList = cart.getCartItemsList();
        List<CartItemsVO> collect = skuList.stream()
                .filter(skuVO -> skuVO.getChecked() == 1 && skuVO.getSingleList() != null).collect(Collectors.toList());

        collect.forEach(skuVO -> {

            List<TradePromotionGoodsVO> promotionList = skuVO.getPromotionList();

            List<TradePromotionGoodsVO> collect2 = promotionList.stream().filter(tradePromotionGoodsVO ->
                    tradePromotionGoodsVO.getPromotionType().equals(PromotionTypeEnum.SECKILL.name()) && tradePromotionGoodsVO.getIsCheck() == 1)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect2)){
                List<TradePromotionGoodsVO> collect1 = skuVO.getSingleList().stream()
                        .filter(promotionGoodsVO -> promotionGoodsVO.getPromotionType()
                                .equals(PromotionTypeEnum.COMPANY_DISCOUNT.name())).collect(Collectors.toList());

                collect1.forEach(promotionGoodsVO -> {

                    // 商品原价
                    Double originalPrice = skuVO.getGoodsPrice();

                    // 商品数量
                    int num = skuVO.getNum();
                    // 商品小计
                    Double subTotal = MathUtil.multiply(originalPrice, num);

                    // 商品优惠后总价
                    Double purchasePrice = MathUtil.multiply(subTotal, promotionGoodsVO.getDiscount());

                    // 此商品的总优惠金额
                    Double goodsDiscountPrice = MathUtil.subtract(subTotal, purchasePrice);
                    // 该商品优惠后的单价
                    skuVO.setThisGoodsPrice(purchasePrice);
                    // 累加店铺优惠金额
                    cart.getPrice().setDiscountPrice(MathUtil.add(cart.getPrice().getDiscountPrice(), goodsDiscountPrice));

                    System.out.println("店铺优惠金额" + goodsDiscountPrice);
                });
            }


        });
    }
}
