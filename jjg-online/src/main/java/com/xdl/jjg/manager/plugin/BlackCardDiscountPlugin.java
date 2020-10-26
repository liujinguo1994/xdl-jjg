package com.xdl.jjg.manager.plugin;

import com.jjg.trade.model.vo.CartItemsVO;
import com.jjg.trade.model.vo.CartVO;
import com.xdl.jjg.manager.event.PromotionEvent;
import com.xdl.jjg.util.MathUtil;
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
public class BlackCardDiscountPlugin implements PromotionEvent {

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
                .filter(skuVO -> skuVO.getChecked() == 1).collect(Collectors.toList());

        collect.forEach(skuVO -> {

                    Double originalPrice = skuVO.getGoodsPrice();

                    int num = skuVO.getNum();

                    Double subTotal = MathUtil.multiply(originalPrice, num);

                    Double purchasePrice = MathUtil.multiply(subTotal, cart.getBlackCardDiscount());

                    Double goodsDiscountPrice = MathUtil.subtract(subTotal, purchasePrice);

                    skuVO.setThisGoodsPrice(purchasePrice);

                    cart.getPrice().setDiscountPrice(MathUtil.add(cart.getPrice().getDiscountPrice(), goodsDiscountPrice));

                    System.out.println("黑卡优惠金额" + goodsDiscountPrice);

        });
    }
}
